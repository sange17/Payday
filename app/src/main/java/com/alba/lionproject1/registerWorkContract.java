package com.alba.lionproject1;

import android.provider.BaseColumns;

public class registerWorkContract {
    private registerWorkContract() {

    }

    public static class registerWorkEntry implements BaseColumns {
        public static final int DB_VERSION = 1;
        public static final String TABLE_NAME = "registerWork";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FROM = "rwfrom";
        public static final String COLUMN_NAME_TO = "rwto";
        public static final String COLUMN_NAME_MEMO = "rwmemo";
        public static final String COLUMN_NAME_ATPHOTOPATH = "path_at";
        public static final String COLUMN_NAME_AFTERPHOTOPATH = "path_after";
        public static final String COLUMN_NAME_TODATE = "todate";
        public static final String _CREAT0 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME_DATE + " TEXT NOT NULL , "
                + COLUMN_NAME_TITLE + " TEXT NOT NULL , "
                + COLUMN_NAME_FROM + " TEXT NOT NULL , "
                + COLUMN_NAME_TO + " TEXT NOT NULL , "
                + COLUMN_NAME_MEMO + " TEXT NOT NULL , "
                + COLUMN_NAME_ATPHOTOPATH + " TEXT NOT NULL, "
                + COLUMN_NAME_AFTERPHOTOPATH + " TEXT NOT NULL , "
                + COLUMN_NAME_TODATE + " TEXT NOT NULL);";
    }
}
