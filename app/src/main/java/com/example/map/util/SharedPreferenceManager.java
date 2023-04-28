package com.example.map.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.map.presentation.model.Location;
import com.google.gson.Gson;

public class SharedPreferenceManager {
    private static final String KEY_IS_FIRST = "KEY_IS_FIRST";
    private static final String KEY_LAST_LOCATION = "KEY_LAST_LOCATION";

    public static boolean isFirst(Context context) {
        return getBoolean(context, KEY_IS_FIRST);
    }

    public static void setFirst(Context context) {
        putBoolean(context, KEY_IS_FIRST, false);
    }

    public static Location getLastLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(KEY_LAST_LOCATION, "");
        if (!jsonString.isEmpty()) {
            return new Gson().fromJson(jsonString, Location.class);
        } else {
            return null;
        }
    }

    public static void setLastLocation(Context context, Location location) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_LOCATION, new Gson().toJson(location));
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, true);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
