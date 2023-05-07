package com.example.map.util

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment

object Logger {

    fun printLifeCycleLog(activity: Activity, methodName: String) {
        Log.d(activity.javaClass.simpleName, methodName)
    }

    fun printLifeCycleLog(fragment: Fragment, methodName: String) {
        Log.d(fragment.javaClass.simpleName, methodName)
    }
}
