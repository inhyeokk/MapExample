package com.example.map.presentation.view.main.mapview

import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

open class BasicMapViewEventListener : MapView.MapViewEventListener {
    override fun onMapViewInitialized(mapView: MapView) {}
    override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {}
    override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {}
    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {}
}
