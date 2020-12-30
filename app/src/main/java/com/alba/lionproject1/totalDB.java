package com.alba.lionproject1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class totalDB extends SQLiteOpenHelper {
    private static totalDB tdb;
    private static final String CREATE_TOTAL_INFORMATION_TABLE = DataBases.CreatDB._CREAT0;
    private static final String DROP_TOTAL_INFORMATION_TABLE = DataBases.CreatDB._DROP0;

    public static totalDB getInstance(Context context) {
        if (tdb == null) {
            tdb = new totalDB(context);
        }
        return tdb;
    }

    public totalDB(@Nullable Context context) {
        super(context, DataBases.CreatDB._TABLENAME0, null, DataBases.CreatDB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOTAL_INFORMATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TOTAL_INFORMATION_TABLE);
        onCreate(db);

    }
}
