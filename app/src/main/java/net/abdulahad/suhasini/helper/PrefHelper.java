package net.abdulahad.suhasini.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class PrefHelper {

    public static boolean putInt(Context context, String key, int value) {
        return getEditor(context).putInt(key, value).commit();
    }

    public static void putIntApply(Context context, String key, int value) {
        getEditor(context).putInt(key, value).apply();
    }


    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);

        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }


    public static boolean putBoolean(Context context, String key, boolean value) {
        return getEditor(context).putBoolean(key, value).commit();
    }

    public static void putBooleanApply(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }


    public static boolean putStringSet(Context context, String key, Set<String> value) {
        return getEditor(context).putStringSet(key, value).commit();
    }

    public static boolean putString(Context context, String key, String value) {
        return getEditor(context).putString(key, value).commit();
    }

    public static void putStringApply(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);

        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);

        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getStringSet(key, defaultValue);
        }
        return defaultValue;
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreference(context).edit();
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getSharedPreference(context).getFloat(key, defaultValue);
    }
}