package com.example.daysremainder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements Fragment1.OnSelectedElementListener/*, NavigationView.OnNavigationItemSelectedListener*/ {
    public int ACTION_TIME = 9;
    public int ACTION_VIBRO = 5;
    private boolean mIsDynamic;
    private int currentElement = 0;
    private DaysRemainderReceiver mDaysRemainderReceiver = new DaysRemainderReceiver();

    public void registerBroadcastReceiver(MenuItem item) {
        this.registerReceiver(mDaysRemainderReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
        Toast.makeText(getApplicationContext(), "Приёмник включен", Toast.LENGTH_LONG).show();
    }

    public void unregisterBroadcastReceiver(MenuItem item) {
        this.unregisterReceiver(mDaysRemainderReceiver);
        Toast.makeText(getApplicationContext(), "Приёмник выключён", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment2 fragment2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragment2);
        mIsDynamic = fragment2 == null || !fragment2.isInLayout();
//        Toast.makeText(getApplicationContext(), mIsDynamic + "", Toast.LENGTH_SHORT).show();
        if (mIsDynamic) {
            // начинаем транзакцию
            FragmentTransaction ft = fragmentManager.beginTransaction();
            // Создаем и добавляем первый фрагмент
            Fragment1 fragment1 = new Fragment1();
            Fragment1 fragment1_old = (Fragment1) fragmentManager.findFragmentById(R.id.fragment1);
            if (fragment1_old == null || !fragment1_old.isInLayout()) {
                ft.replace(R.id.container, fragment1, "fragment1");
            } else {
                ft.add(R.id.container, fragment1, "fragment1");
            }
            // Подтверждаем операцию
            ft.commit();
        }
        //

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ACTION_TIME = Integer.parseInt(prefs.getString(getString(R.string.action_time), "9"));
        ACTION_VIBRO = Integer.parseInt(prefs.getString(getString(R.string.action_vibro), "5"));
    }

    @Override
    public void onElementSelected(int buttonIndex) {
        // подключаем FragmentManager
        currentElement = buttonIndex;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment2 fragment2;
        if (mIsDynamic) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment2 = new Fragment2();
            Bundle args = new Bundle();
            args.putInt(Fragment2.ELEMENT_INDEX, buttonIndex);
            fragment2.setArguments(args);
            ft.replace(R.id.container, fragment2, "fragment2");
            ft.addToBackStack(null);
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.commit();
        } else {
            // Если фрагмент доступен
            fragment2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragment2);
            fragment2.setValues(buttonIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        switch (item.getItemId()) {
            case R.id.action_new:
                onElementSelected(-1);
                return true;
            case R.id.action_contacts:
                showNotImplemented("Загрузка контактов из книги контактов телефона");
                //TODO
                return true;
            case R.id.action_find:
                showNotImplemented("Поиск по имени");
                //TODO
                return true;
            case R.id.action_delete:
                if (currentElement == 0) {
                    Toast.makeText(this, "Сначала выберите контакт", Toast.LENGTH_LONG).show();
                } else {
                    final DaysRemainderDbHelper dr_db = new DaysRemainderDbHelper(this);
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Удаление контакта")
                            .setMessage("Программа удалит информацию о контакте только из СВОЕЙ базы данных, контакты в телефонной книге НЕ ИЗМЕНЯТСЯ!!! \n\nВы действительно хотите удалить контакт?")
                            .setPositiveButton("Да, удалить!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SQLiteDatabase db = dr_db.getWritableDatabase();
                                    db.delete("contacts", "_id = " + currentElement, null);
                                    db.close();
//                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Нет, подождем.", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
                                }
                            })
                            .setCancelable(true)
                            .create()
                            .show();
                    dr_db.close();
//                    getFragmentManager().popBackStack();
                }
                return true;
            case R.id.action_clear:
                final DaysRemainderDbHelper dr_db = new DaysRemainderDbHelper(this);
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Удаление всех контакта")
                        .setMessage("Программа удалит все контакті только из СВОЕЙ базы данных, контакты в телефонной книге НЕ ИЗМЕНЯТСЯ!!! \n\nВы действительно хотите удалить ВСЕ?")
                        .setPositiveButton("Да, удалить!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = dr_db.getWritableDatabase();
                                db.delete("contacts", null, null);
                                db.close();
//                                    dialog.cancel();
                            }
                        })
                        .setNegativeButton("Нет, подождем.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
                            }
                        })
                        .setCancelable(true)
                        .create()
                        .show();
                dr_db.close();
                return true;
            case R.id.action_professions:
                showNotImplemented("Редактирование списка профессий");
                //TODO
                return true;
            case R.id.action_holidays:
                showNotImplemented("Редактирование праздничных дней");
                //TODO
                return true;
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
//            case R.id.action_reg_receiver:
//                registerBroadcastReceiver();
//                return true;
//            case R.id.action_unreg_receiver:
//                unregisterBroadcastReceiver();
//                return true;
            case R.id.action_info:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("О программе")
                        .setMessage("Программа напоминания о днях роджений и других праздниках\n"
                                + "Позволяет редактировать информацию, уведомлять и отправлять SMS")
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                dialog.cancel();
                            }
                        }).create().show();
                return true;
            default:
                return true;
        }
    }

    private void showNotImplemented(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage("Этот пункт меню еще не реализован")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем окно
                        dialog.cancel();
                    }
                }).create().show();
    }
}
