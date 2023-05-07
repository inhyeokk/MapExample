package com.example.map.presentation.screen.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.IntentCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.map.R
import com.example.map.base.BaseAppCompatActivity
import com.example.map.extension.isEnabled
import com.example.map.extension.showToast
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.DocumentResult
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.screen.favorite.FavoriteActivity
import com.example.map.presentation.screen.main.entity.ListMode
import com.example.map.presentation.screen.main.entity.MapViewMode
import com.example.map.presentation.screen.main.entity.SearchType
import com.example.map.presentation.screen.main.entity.SelectPosition
import com.example.map.presentation.screen.main.mapview.BasicMapViewEventListener
import com.example.map.presentation.screen.main.mapview.BasicPOIItemEventListener
import com.example.map.presentation.screen.search.SearchActivity
import com.example.map.util.AccessFineLocationUtil
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.*
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseAppCompatActivity() {
    private var mapView: MapView? = null
    private val viewModel by viewModels<MainViewModel>()
    private val searchResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { data ->
                    setSearchResultInMapView(IntentCompat.getParcelableExtra(data, SearchActivity.SEARCH_RESULT, SearchResult::class.java)!!)
                }
            }
        }

    private fun setSearchResultInMapView(searchResult: SearchResult) {
        mapView!!.removeAllPOIItems()
        val mapPoint = MapPoint.mapPointWithGeoCoord(searchResult.y(), searchResult.x()).apply {
            addPOIItemInMapView(this, searchResult.addressName)
        }
        mapView!!.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint))
    }

    private val mapViewEventListener = object : BasicMapViewEventListener() {
        override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
            viewModel.disableTrackingMode()
        }

        override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
            val searchType = SearchType.from(viewModel.mapViewMode)
            if (searchType != null) {
                requestSearch(searchType, false)
            }
        }
    }
    private val poiItemEventListener = object : BasicPOIItemEventListener() {
        override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {
            if (viewModel.mapViewMode.isNotDefault) {
                selectDocumentBy(mapPOIItem)
            }
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPOIItem.mapPoint))
        }
    }

    private fun selectDocumentBy(mapPOIItem: MapPOIItem) {
        val documentList = viewModel.requireDocumentList
        documentList.find { it.mapPOIItem == mapPOIItem }?.let {
            val index = documentList.indexOf(it)
            viewModel.selectDocument(index, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val mapViewMode by viewModel.mapViewModeLiveData.observeAsState(initial = MapViewMode.DEFAULT)
                val documentResult by viewModel.documentResultEvent.observeAsState(initial = DocumentResult.empty())
                val selectPositionEvent by viewModel.selectPositionEvent.observeAsState(initial = SelectPosition())
                val trackingMode by viewModel.trackingModeLiveData.observeAsState(initial = MapView.CurrentLocationTrackingMode.TrackingModeOff)
                val listMode by viewModel.listModeLiveData.observeAsState(initial = ListMode.LIST)
                MainScreen(
                    mapViewMode = mapViewMode,
                    documentResult = documentResult,
                    selectPositionEvent = selectPositionEvent,
                    trackingMode = trackingMode,
                    listMode = listMode,
                    onDocumentClick = { document, position -> onDocumentClick(document, position) },
                    onDocumentFavoriteClick = { viewModel.addFavoriteDocument(it) },
                    onDocumentUnFavoriteClick = { viewModel.removeFavoriteDocument(it) },
                    onBackClick = { onBackPressed() },
                    onSearchClick = { startSearchActivity() },
                    onFoodClick = { requestSearch(SearchType.FOOD, true) },
                    onCafeClick = { requestSearch(SearchType.CAFE, true) },
                    onConvenienceClick = { requestSearch(SearchType.CONVENIENCE, true) },
                    onFlowerClick = { requestSearch(SearchType.FLOWER, true) },
                    onFavoriteClick = { startActivity(FavoriteActivity::class.java) },
                    onTrackingModeClick = { viewModel.toggleTrackingMode() },
                    onListModeClick = { viewModel.toggleListMode() },
                ) {
                    AndroidView(
                        factory = {
                            MapView(this).apply {
                                setMapViewEventListener(this@MainActivity.mapViewEventListener)
                                setPOIItemEventListener(poiItemEventListener)
                            }.also {
                                mapView = it.apply {
                                    currentLocationTrackingMode = trackingMode
                                }
                            }
                        },
                    )
                }
            }
        }
        observeViewModel()
    }

    private fun onDocumentClick(document: Document, position: Int) {
        viewModel.selectDocument(position, false)
        mapView!!.selectPOIItem(document.mapPOIItem, true)
        mapView!!.moveCamera(CameraUpdateFactory.newMapPoint(document.mapPOIItem!!.mapPoint))
    }

    private fun startSearchActivity() {
        viewModel.disableTrackingMode()
        val intent = Intent(this, SearchActivity::class.java)
        searchResultLauncher.launch(intent)
    }

    private fun requestSearch(searchType: SearchType, isMoveCamera: Boolean) {
        val mapPoint = mapView!!.mapCenterPoint.mapPointGeoCoord
        viewModel.search(
            searchType, mapPoint.longitude.toString(), mapPoint.latitude.toString(), isMoveCamera
        )
    }

    private fun startActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.mapViewModeLiveData.observe(this) {
            if (it.isDefault) {
                mapView?.removeAllPOIItems()
            }
        }
        viewModel.trackingModeLiveData.observe(this) {
            if (it.isEnabled()) {
                AccessFineLocationUtil.checkPermission(this, {
                    mapView?.currentLocationTrackingMode = it
                }, {
                    viewModel.tempTrackingModeForEnable = it
                }) {
                    showToast(R.string.Toast_location_permission_denied)
                }
            } else {
                mapView?.currentLocationTrackingMode = it
            }
        }
        viewModel.documentResultEvent.observe(this) {
            mapView!!.removeAllPOIItems()
            val mapPointList = it.documentList.map { document ->
                MapPoint.mapPointWithGeoCoord(document.y(), document.x()).apply {
                    document.mapPOIItem = addPOIItemInMapView(
                        mapPoint = this,
                        name = "${document.placeName} ${String.format(Locale.getDefault(), "%.1f", document.rate)}"
                    )
                }
            }
            if (it.isMoveCamera) {
                val mapPointBounds = MapPointBounds(mapPointList.toTypedArray())
                mapView!!.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100))
            }
        }
        viewModel.mainActionEvent.observe(this) {
            when (it) {
                MainAction.SEARCH_FAILURE -> showToast(R.string.Toast_search_failure)
                MainAction.EMPTY_SEARCH -> showToast(R.string.Toast_empty_search)
                MainAction.EMPTY_FAVORITE -> showToast(R.string.Toast_empty_favorite)
                else -> {}
            }
        }
    }

    private fun addPOIItemInMapView(mapPoint: MapPoint, name: String): MapPOIItem {
        return MapPOIItem().apply {
            itemName = name
            isShowDisclosureButtonOnCalloutBalloon = false
            this.mapPoint = mapPoint
            markerType = MapPOIItem.MarkerType.BluePin
        }.also {
            mapView!!.addPOIItem(it)
        }
    }

    override fun onBackPressed() {
        if (viewModel.mapViewMode.isNotDefault) {
            viewModel.mapViewMode = MapViewMode.DEFAULT
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AccessFineLocationUtil.onRequestPermissionsResult(requestCode, grantResults, {
            viewModel.enableTrackingMode()
        }) {
            viewModel.disableTrackingMode()
            showToast(R.string.Toast_location_permission_denied)
        }
    }

    override fun onDestroy() {
        mapView = null
        super.onDestroy()
    }
}
