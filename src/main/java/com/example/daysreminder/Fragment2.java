package com.example.daysreminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Fragment2 extends Fragment {

    public static final String ELEMENT_INDEX = "element_index";
    private static final int ELEMENT_INDEX_DEFAULT = -1;

    private TextView mRecordNumberText;
    private EditText mEditName, mEditEmail, mEditPhones, mEditProfession, mEditBirthDay, mEditBirthMonth, mEditBirthYear;
    private Button mButtonSave;
    private CheckBox mHasChildren, mIsFriend;
    private Spinner mEditProfessionSpinner;
    private boolean editMode;
    private int elementIndex;

    private DaysReminderDbHelper dr_db;

    private String[] professionNames;
    private int[] professionIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment2, container, false);
        mRecordNumberText = rootView.findViewById(R.id.record_number);
        mEditName = rootView.findViewById(R.id.edit_name);
        mEditEmail = rootView.findViewById(R.id.edit_email);
        mEditPhones = rootView.findViewById(R.id.edit_phones);
        mEditBirthDay = rootView.findViewById(R.id.edit_birth_day);
        mEditBirthMonth = rootView.findViewById(R.id.edit_birth_month);
        mEditBirthYear = rootView.findViewById(R.id.edit_birth_year);
        mEditProfession = rootView.findViewById(R.id.edit_profession);
        mEditProfessionSpinner = rootView.findViewById(R.id.edit_profession_spinner);
        mButtonSave = rootView.findViewById(R.id.button_save);

        mHasChildren = rootView.findViewById(R.id.edit_has_children);
        mIsFriend = rootView.findViewById(R.id.edit_is_friend);

        dr_db = new DaysReminderDbHelper(getContext());

        SQLiteDatabase db = dr_db.getReadableDatabase();
        Cursor c = db.rawQuery("select _id, name from profession order by name", null);
        professionNames = new String[]{};
        professionIndex = new int[]{};
        if (c != null) {
            if (c.moveToFirst()) {
                professionNames = new String[c.getCount()];
                professionIndex = new int[c.getCount()];
                int counter = 0;
                do {
                    professionIndex[counter] = Integer.valueOf(c.getString(c.getColumnIndex("_id")));
                    professionNames[counter] = c.getString(c.getColumnIndex("name"));
                    counter++;
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, professionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEditProfessionSpinner.setAdapter(adapter);
        mEditProfessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mEditProfession.setText(String.valueOf(professionIndex[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Bundle args = getArguments();
        elementIndex = args != null ? args.getInt(ELEMENT_INDEX, ELEMENT_INDEX_DEFAULT) : ELEMENT_INDEX_DEFAULT;
        editMode = false;
        if (elementIndex != ELEMENT_INDEX_DEFAULT) {
            editMode = true;
            setValues(elementIndex);
        }

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean everythingOk = true;
                if (mEditName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Введите правильно полное имя", Toast.LENGTH_LONG).show();
                    everythingOk = false;
                }
                if (!(Integer.parseInt(mEditBirthDay.getText().toString()) > 0 && Integer.parseInt(mEditBirthDay.getText().toString()) < 32)) {
                    Toast.makeText(getContext(), "Введите правильно день", Toast.LENGTH_LONG).show();
                    everythingOk = false;
                }
                if (!(Integer.parseInt(mEditBirthMonth.getText().toString()) > 0 && Integer.parseInt(mEditBirthMonth.getText().toString()) < 13)) {
                    Toast.makeText(getContext(), "Введите правильно месяц", Toast.LENGTH_LONG).show();
                    everythingOk = false;
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                if (!(Integer.parseInt(mEditBirthYear.getText().toString()) > 1940 && Integer.parseInt(mEditBirthYear.getText().toString()) < year)) {
                    Toast.makeText(getContext(), "Введите правильно год", Toast.LENGTH_LONG).show();
                    everythingOk = false;
                }
                if (everythingOk) {
                    DaysReminderDbHelper dr_db = new DaysReminderDbHelper(getContext());
                    SQLiteDatabase db = dr_db.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put("name", mEditName.getText().toString());
                    cv.put("email", mEditEmail.getText().toString());
                    cv.put("phones", mEditPhones.getText().toString());
                    cv.put("has_children", mHasChildren.isChecked() ? 1 : 0);
                    cv.put("is_friend", mIsFriend.isChecked() ? 1 : 0);
                    cv.put("year", mEditBirthYear.getText().toString());
                    cv.put("month", mEditBirthMonth.getText().toString());
                    cv.put("day", mEditBirthDay.getText().toString());
                    cv.put("profession_id", mEditProfession.getText().toString());
                    if (editMode) {
                        int updCount = db.update("contacts", cv, "_id = ?", new String[]{String.valueOf(elementIndex)});
                        if (updCount != 1) {
                            Toast.makeText(getContext(), "Что-то пошло не так, обновлено " + updCount + " записей", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        long rowID = db.insert("contacts", null, cv);
                        Toast.makeText(getContext(), "Добавлена запись № " + rowID, Toast.LENGTH_LONG).show();
                    }
                    db.close();
                    dr_db.close();
                    getFragmentManager().popBackStack();
                }
            }
        });

        return rootView;
    }

    public void setValues(int elementIndex) {

        SQLiteDatabase db = dr_db.getReadableDatabase();
        mRecordNumberText.setText("Record number " + elementIndex);
        Cursor c = db.rawQuery("select * from contacts where _id = " + elementIndex, null);
        if (c != null) {
            if (c.moveToFirst()) {
                mEditName.setText(c.getString(c.getColumnIndex("name")));
                mEditBirthYear.setText(c.getString(c.getColumnIndex("year")));
                mEditBirthMonth.setText(c.getString(c.getColumnIndex("month")));
                mEditBirthDay.setText(c.getString(c.getColumnIndex("day")));
                mEditEmail.setText(c.getString(c.getColumnIndex("email")));
                mEditPhones.setText(c.getString(c.getColumnIndex("phones")));
                mEditProfession.setText(c.getString(c.getColumnIndex("profession_id")));
                int profession_id = Integer.parseInt(mEditProfession.getText().toString());
                int id_profession;
                for (id_profession = 0; id_profession < professionIndex.length; id_profession++) {
                    if (professionIndex[id_profession] == profession_id) break;
                }
                mEditProfessionSpinner.setSelection(id_profession);
                mHasChildren.setChecked(Integer.parseInt(c.getString(c.getColumnIndex("has_children"))) == 0 ? false : true);
                mIsFriend.setChecked(Integer.parseInt(c.getString(c.getColumnIndex("is_friend"))) == 0 ? false : true);
            }
        }
        c.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        dr_db.close();
    }
}
