package com.handy.test2.Model;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    private final static String MY_PREFS_NAME = "MyPreferences";
    public final static String FROM_POSITION_KEY = "fromPosition";
    public final static String TO_POSITION_KEY = "toPosition";
    public final static String FROM_KEY = "from";
    public final static String TO_KEY = "to";
    public final static String MONITOR_STATE_KEY = "monitorState";

    public static void setState(Context context, boolean enable) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(MONITOR_STATE_KEY, enable);
        editor.apply();
    }

    public static boolean getState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(MONITOR_STATE_KEY, false);
    }

    public static void setExchangeRate(Context context, int i1, int i2, float from, float to) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(FROM_POSITION_KEY, i1);
        editor.putInt(TO_POSITION_KEY, i2);
        editor.putFloat(FROM_KEY , from);
        editor.putFloat(TO_KEY , to);
        editor.apply();
    }

    public static HashMap<String, Object> getExchangeRate(Context context) {
        HashMap<String, Object> hashMap = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int i1 =sharedPreferences.getInt(FROM_POSITION_KEY, 0);
        int i2 = sharedPreferences.getInt(TO_POSITION_KEY, 0);
        float to = sharedPreferences.getFloat(TO_KEY, 0);
        float from = sharedPreferences.getFloat(FROM_KEY, 0);
        hashMap.put(FROM_POSITION_KEY, i1);
        hashMap.put(TO_POSITION_KEY, i2);
        hashMap.put(TO_KEY, to);
        hashMap.put(FROM_KEY, from);
        return hashMap;
    }

}
