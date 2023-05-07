package com.example.map.base

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.map.util.Logger

open class BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.printLifeCycleLog(this, "onCreate savedInstanceState is null " + (savedInstanceState == null))
    }

    override fun onStart() {
        super.onStart()
        Logger.printLifeCycleLog(this, "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Logger.printLifeCycleLog(this, "onRestart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.printLifeCycleLog(this,"onRestoreInstanceState")
    }

    override fun onResume() {
        super.onResume()
        Logger.printLifeCycleLog(this,"onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.printLifeCycleLog(this,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.printLifeCycleLog(this, "onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Logger.printLifeCycleLog(this,"onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.printLifeCycleLog(this, "onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logger.printLifeCycleLog(this, "onConfigurationChanged")
    }

}
