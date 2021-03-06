package com.example.desserts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dessertsDb";
    public static final String TABLE_DESSERTS = "desserts";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_CCAL = "ccal";
    public static final String KEY_PRICE = "price";


    public DBHelper(MainActivity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_DESSERTS + " (" +
                KEY_ID + " integer primary key," +
                KEY_NAME + " text," +
                KEY_WEIGHT + " float," +
                KEY_CCAL + " float," +
                KEY_PRICE + " float" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_DESSERTS);
        onCreate(db);
    }
}
