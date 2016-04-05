package com.atang.androidtodo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by atang on 3/29/16.
 */
public class TodoSqlLiteHelper extends SQLiteOpenHelper {

    // Example from
    // http://guides.codepath.com/android/Local-Databases-with-SQLiteOpenHelper
    public TodoSqlLiteHelper(Context context){
        super(context, "todo_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table todos(_id INTEGER PRIMARY KEY AUTOINCREMENT,todo TEXT NOT NULL,priority TEXT NOT NULL,date TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS todos");
            onCreate(db);
        }
    }

}
