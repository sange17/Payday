package com.alba.lionproject1;

import android.content.Context;
import android.content.SharedPreferences;


// 데이터 저장 및 로드 클래스

public class PreferenceManager {

    public static final String PREFERENCES_NAME = "rebuild_preference";


    private static final String DEFAULT_VALUE_STRING = "";


    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    public static void setString(Context context, String key, String value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);

        editor.apply();

    }

    public static void setInt(Context context, String key, int value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(key, value);

        editor.apply();

    }

    public static String getString(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        return prefs.getString(key, DEFAULT_VALUE_STRING);
    }
}

