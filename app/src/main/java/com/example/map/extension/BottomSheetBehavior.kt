package com.example.map.extension

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun <T : View> BottomSheetBehavior<T>.setVisible(isVisible: Boolean) {
    val updatedState =
        if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
    state = updatedState
}
