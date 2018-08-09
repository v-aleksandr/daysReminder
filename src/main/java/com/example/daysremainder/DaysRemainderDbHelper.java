package com.example.daysremainder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DaysRemainderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "daysremainder.db";
    private static final int DATABASE_VERSION = 1;

    public DaysRemainderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        ContentValues cv = new ContentValues();
        db.beginTransaction();
        try {
            db.execSQL("CREATE TABLE profession ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name VARCHAR(50) NOT NULL, "
                    + "month INT NOT NULL DEFAULT 1, day INT NOT NULL DEFAULT 1, "
                    + "template TEXT NOT NULL);");
            db.execSQL("INSERT INTO profession VALUES (1, 'таможенник', 1, 26, 'Примите мои поздравления с Международным днем таможенника!');");
            db.execSQL("INSERT INTO profession VALUES (2, 'бухгалтер', 7, 16, 'Примите мои поздравления с днем бухгалтера Украины!');");
            db.execSQL("INSERT INTO profession VALUES (3, 'археолог', 8, 15, 'Примите мои поздравления с днем археолога Украины!');");
//           other way
//            cv.clear();
//            cv.put("name", 'таможенник');
//            cv.put("month", 1);
//            cv.put("day", 26);
//            cv.put("template", 'Примите мои поздравления с Международным днем таможенника!');
//            db.insert("profession", null, cv);

            db.execSQL("CREATE TABLE holidays("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name VARCHAR(50)NOT NULL, "
                    + "month INT NOT NULL DEFAULT 1, day INT NOT NULL DEFAULT 1, "
                    + "template TEXT NOT NULL);");
            db.execSQL("INSERT INTO holidays VALUES (1, 'Новый Год', 1, 1, 'С НОВЫМ ГОДОМ!!!');");
            db.execSQL("INSERT INTO holidays VALUES (2, 'Рождество Христово', 1, 7, 'С Рождеством Христовым!!!')");
            db.execSQL("INSERT INTO holidays VALUES (3, 'День Святого Валентина', 2, 14, 'Поздравляю с праздником всех влюбленных - днем Святого Валентина! И пусть любовь пребудет с Вами ВСЕГДА!!!')");

            db.execSQL("CREATE TABLE contacts ("
                            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "name VARCHAR(70) NOT NULL, "
                            + "year INT NOT NULL DEFAULT 1900, month INT NOT NULL DEFAULT 1, day INT NOT NULL DEFAULT 1, "
                            + "email VARCHAR(50), "
                            + "phones VARCHAR(50), "
                            + "profession_id INTEGER, "
                            + "has_children INTEGER NOT NULL DEFAULT 0, "
                            + "is_friend INTEGER NOT NULL DEFAULT 0);");
            db.execSQL("INSERT INTO contacts VALUES(1,'Грабовский Олег Борисович',1954,8,1,'','+380636627271,+380674017733,+380955489722',1,4,1);");
            db.execSQL("INSERT INTO contacts VALUES(2,'Василец Александр Иванович',1947,6,4,'','+380661716979',3,1,1);");
            db.execSQL("INSERT INTO contacts VALUES(3,'Яситникова Людмила Васильевна',1945,5,1,'','+380504462135',2,0,0);");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
