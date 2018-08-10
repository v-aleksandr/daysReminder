package com.example.daysreminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Fragment1 extends ListFragment {
    private String[] contactsNames;
    private int[] contactsIndex;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaysReminderDbHelper dr_db = new DaysReminderDbHelper(getContext());
        SQLiteDatabase db = dr_db.getReadableDatabase();
        fillContactsLists(db);
        db.close();
        dr_db.close();

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, contactsNames);
        setListAdapter(adapter);
    }

    private void fillContactsLists(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select _id, name from contacts order by name", null);
        contactsNames = new String[]{};
        contactsIndex = new int[]{};
        if (c != null) {
            if (c.moveToFirst()) {
                contactsNames = new String[c.getCount()];
                contactsIndex = new int[c.getCount()];
                int counter = 0;
                do {
                    contactsIndex[counter] = Integer.valueOf(c.getString(c.getColumnIndex("_id")));
                    contactsNames[counter] = c.getString(c.getColumnIndex("name"));
                    counter++;
                } while (c.moveToNext());
            }
        }
        c.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        OnSelectedElementListener listener = (OnSelectedElementListener) getActivity();
        listener.onElementSelected(contactsIndex[position]);

    }

    public interface OnSelectedElementListener {
        void onElementSelected(int elementIndex);
    }
}
