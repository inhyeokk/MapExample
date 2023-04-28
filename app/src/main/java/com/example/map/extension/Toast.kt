package com.example.map.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.show(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.show(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}
