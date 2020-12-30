package com.alba.lionproject1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class registerWorkDBHelper extends SQLiteOpenHelper {
    public static registerWorkDBHelper sInstance;

    public static final String SQL_CREATE_ENTRIES = registerWorkContract.registerWorkEntry._CREAT0;
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + registerWorkContract.registerWorkEntry.TABLE_NAME;

    public static registerWorkDBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new registerWorkDBHelper(context);
        }
        return sInstance;
    }

    public registerWorkDBHelper(@Nullable Context context) {
        super(context, registerWorkContract.registerWorkEntry.TABLE_NAME, null, registerWorkContract.registerWorkEntry.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
