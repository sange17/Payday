package com.alba.lionproject1;

import android.provider.BaseColumns;

public class DataBases {

    public static final class CreatDB implements BaseColumns {
        public static final String _TABLENAME0 = "usertable";
        public static final int DB_VERSION = 1;
        public static final String PARTTIMENAME = "parttimename";
        public static final String HOURLYWAGE = "wage";
        public static final String DAYOFWEEK = "dayofweek";
        public static final String WORKINGSTART = "workingstart";
        public static final String WORKINGFINISH = "workingfinish";
        public static final String NUMBER_OF_PEOPLE = "numberofpeople";
        public static final String MEAL_TIME = "mealtime";
        public static final String _DROP0 = "DROP TABLE IF EXISTS " + _TABLENAME0;
        public static final String _CREAT0 = "CREATE TABLE IF NOT EXISTS " + _TABLENAME0 + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PARTTIMENAME + " TEXT NOT NULL , "
                + HOURLYWAGE + " TEXT NOT NULL , "
                + DAYOFWEEK + " TEXT NOT NULL , "
                + WORKINGSTART + " TEXT NOT NULL , "
                + WORKINGFINISH + " TEXT NOT NULL , "
                + NUMBER_OF_PEOPLE + " TEXT NOT NULL , "
                + MEAL_TIME + " TEXT NOT NULL );";
    }
}
