package com.example.map.util

import android.content.Context
import androidx.core.content.edit
import com.example.map.presentation.model.Location
import com.google.gson.Gson

object SharedPreferenceManager {
    private const val KEY_IS_FIRST = "KEY_IS_FIRST"
    private const val KEY_LAST_LOCATION = "KEY_LAST_LOCATION"
    fun isFirst(context: Context): Boolean {
        return getBoolean(context, KEY_IS_FIRST)
    }

    fun setFirst(context: Context) {
        putBoolean(context, KEY_IS_FIRST, false)
    }

    fun getLastLocation(context: Context): Location? {
        val sharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(KEY_LAST_LOCATION, "") ?: ""
        return if (jsonString.isNotEmpty()) {
            Gson().fromJson(jsonString, Location::class.java)
        } else {
            null
        }
    }

    fun setLastLocation(context: Context, location: Location) {
        val sharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(KEY_LAST_LOCATION, Gson().toJson(location))
        }
    }

    private fun getBoolean(context: Context, key: String): Boolean {
        val sharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, true)
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        val sharedPreferences =
            context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }
}
