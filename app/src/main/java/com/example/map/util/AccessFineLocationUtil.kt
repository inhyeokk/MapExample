package com.example.map.util

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.map.R

object AccessFineLocationUtil {
    private const val PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE = 1000
    fun requestPermission(activity: Activity) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (SharedPreferenceManager.isFirst(activity) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission
            )
        ) {
            SharedPreferenceManager.setFirst(activity)
            ActivityCompat.requestPermissions(
                activity, arrayOf(permission),
                PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE
            )
        }
    }

    fun checkPermission(
        activity: Activity,
        onGranted: Runnable,
        onRequest: Runnable,
        onCanceled: Runnable
    ) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted.run()
        } else if (SharedPreferenceManager.isFirst(activity) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission
            )
        ) {
            SharedPreferenceManager.setFirst(activity)
            ActivityCompat.requestPermissions(
                activity, arrayOf(permission),
                PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE
            )
        } else {
            AlertDialog.Builder(activity)
                .setMessage(R.string.AlertDialog_message_request_location_permission)
                .setPositiveButton(R.string.AlertDialog_button_ok) { dialog: DialogInterface?, which: Int ->
                    onRequest.run()
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + activity.packageName)
                    )
                    activity.startActivity(intent)
                }
                .setNegativeButton(R.string.AlertDialog_button_cancel) { dialog: DialogInterface?, which: Int -> onCanceled.run() }
                .setOnCancelListener { dialog: DialogInterface? -> onCanceled.run() }.show()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        onGranted: Runnable,
        onDenied: Runnable
    ) {
        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onGranted.run()
            } else {
                onDenied.run()
            }
        }
    }
}
