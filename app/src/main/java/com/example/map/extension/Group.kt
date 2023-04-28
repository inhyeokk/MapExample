package com.example.map.extension

import android.view.View
import androidx.constraintlayout.widget.Group

fun Group.setAllOnClickListener(listener: View.OnClickListener) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}
