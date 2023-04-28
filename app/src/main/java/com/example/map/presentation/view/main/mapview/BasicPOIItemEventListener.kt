package com.example.map.presentation.view.main.mapview

import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.POIItemEventListener

open class BasicPOIItemEventListener : POIItemEventListener {
    override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {}

    @Deprecated("Deprecated in Java")
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView, mapPOIItem: MapPOIItem, calloutBalloonButtonType: CalloutBalloonButtonType
    ) {
    }

    override fun onDraggablePOIItemMoved(
        mapView: MapView, mapPOIItem: MapPOIItem, mapPoint: MapPoint
    ) {
    }
}
