package com.example.map.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.map.R;

public class AccessFineLocationUtil {
    private static final int PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE = 1000;

    public static void requestPermission(@NonNull Activity activity) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (SharedPreferenceManager.isFirst(activity) || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            SharedPreferenceManager.setFirst(activity);
            ActivityCompat.requestPermissions(
                activity,
                new String[]{permission},
                PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE
            );
        }
    }

    public static void checkPermission(
        @NonNull Activity activity,
        @NonNull Runnable onGranted,
        @NonNull Runnable onRequest,
        @NonNull Runnable onCanceled
    ) {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            onGranted.run();
        } else if (SharedPreferenceManager.isFirst(activity) || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            SharedPreferenceManager.setFirst(activity);
            ActivityCompat.requestPermissions(
                activity,
                new String[]{permission},
                PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE
            );
        } else {
            new AlertDialog.Builder(activity)
                .setMessage(R.string.AlertDialog_message_request_location_permission)
                .setPositiveButton(R.string.AlertDialog_button_ok, (dialog, which) -> {
                    onRequest.run();
                    Intent intent = new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + activity.getPackageName())
                    );
                    activity.startActivity(intent);
                }).setNegativeButton(R.string.AlertDialog_button_cancel, (dialog, which) -> {
                    onCanceled.run();
                }).setOnCancelListener(dialog -> {
                    onCanceled.run();
                }).show();
        }
    }

    public static void onRequestPermissionsResult(
        int requestCode,
        @NonNull int[] grantResults,
        @NonNull Runnable onGranted,
        @NonNull Runnable onDenied
    ) {
        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onGranted.run();
            } else {
                onDenied.run();
            }
        }
    }
}
