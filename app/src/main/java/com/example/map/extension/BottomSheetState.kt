package com.example.map.extension

import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi

@OptIn(ExperimentalMaterialApi::class)
suspend fun BottomSheetState.toggle() {
    if (isCollapsed) {
        expand()
    } else {
        collapse()
    }
}
