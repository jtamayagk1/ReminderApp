package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";

    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_CONTACT =
            "create table reminder (_id integer primary key autoincrement, "
                    + "task text not null, priority int, "
                    + "date text, time text, details text);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("ALTER TABLE reminder ADD COLUMN details text");
        }catch (Exception ex){

        }
    }
}
