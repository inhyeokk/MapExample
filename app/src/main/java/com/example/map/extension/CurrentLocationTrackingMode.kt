package com.example.map.extension

import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode

fun CurrentLocationTrackingMode.isEnabled(): Boolean {
    return this != CurrentLocationTrackingMode.TrackingModeOff
}
