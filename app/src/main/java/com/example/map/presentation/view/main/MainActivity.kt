package com.example.map.presentation.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import com.example.map.R
import com.example.map.databinding.ActivityMainBinding
import com.example.map.extension.isEnabled
import com.example.map.extension.setVisible
import com.example.map.extension.showToast
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.DocumentResult
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.view.favorite.FavoriteActivity
import com.example.map.presentation.view.main.entity.ListMode
import com.example.map.presentation.view.main.entity.MapViewMode
import com.example.map.presentation.view.main.entity.SearchType
import com.example.map.presentation.view.main.entity.SelectPosition
import com.example.map.presentation.view.main.mapview.BasicMapViewEventListener
import com.example.map.presentation.view.main.mapview.BasicPOIItemEventListener
import com.example.map.presentation.view.search.SearchActivity
import com.example.map.util.AccessFineLocationUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.*
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mapView: MapView? = null
    private val viewModel by viewModels<MainViewModel>()
    private val searchResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { data ->
                    setSearchResultInMapView(data.getSerializableExtra(SearchActivity.SEARCH_RESULT) as SearchResult)
                }
            }
        }

    private fun setSearchResultInMapView(searchResult: SearchResult) {
        mapView!!.removeAllPOIItems()
        val mapPoint = MapPoint.mapPointWithGeoCoord(searchResult.y(), searchResult.x())
        addPOIItemInMapView(mapPoint, searchResult.addressName)
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
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            cvTop.setContent {
                val mapViewMode by viewModel.mapViewModeLiveData.observeAsState(initial = MapViewMode.DEFAULT)
                Column {
                    MainTopAppBar(
                        mapViewMode = mapViewMode,
                        onBackClick = { onBackPressed() },
                        onSearchClick = { startSearchActivity() },
                    )
                    MainButtonRow(
                        mapViewMode = mapViewMode,
                        onFoodClick = { requestSearch(SearchType.FOOD, true) },
                        onCafeClick = { requestSearch(SearchType.CAFE, true) },
                        onConvenienceClick = { requestSearch(SearchType.CONVENIENCE, true) },
                        onFlowerClick = { requestSearch(SearchType.FLOWER, true) },
                        onFavoriteClick = { startActivity(FavoriteActivity::class.java) },
                    )
                }
            }
            cvBottomSheet.setContent {
                val lazyListState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                val documentResult by viewModel.documentResultEvent.observeAsState(initial = DocumentResult.empty())
                val selectPositionEvent by viewModel.selectPositionEvent.observeAsState(initial = SelectPosition())
                if (selectPositionEvent.position != -1 && selectPositionEvent.selectedByMap) {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            lazyListState.scrollToItem(selectPositionEvent.position)
                        }
                    }
                }
                BottomSheet(
                    state = lazyListState,
                    documentList = documentResult.documentList,
                    selectedPosition = selectPositionEvent.position,
                    onDocumentClick = { document, position -> onDocumentClick(document, position) },
                    onFavoriteClick = { viewModel.addFavoriteDocument(it) },
                    onUnFavoriteClick = { viewModel.removeFavoriteDocument(it) },
                )
            }
        }
        setContentView(binding.root)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.cvBottomSheet).apply {
            isDraggable = false
        }
        initView(binding)
        observeViewModel(binding, bottomSheetBehavior)
    }

    private fun onDocumentClick(document: Document, position: Int) {
        viewModel.selectDocument(position, false)
        mapView!!.selectPOIItem(document.mapPOIItem, true)
        mapView!!.moveCamera(CameraUpdateFactory.newMapPoint(document.mapPOIItem!!.mapPoint))
    }

    private fun initView(binding: ActivityMainBinding) {
        initMapView(binding)
        initBtnListener(binding)
    }

    private fun initMapView(binding: ActivityMainBinding) {
        mapView = MapView(this).apply {
            setMapViewEventListener(this@MainActivity.mapViewEventListener)
            setPOIItemEventListener(poiItemEventListener)
        }
        binding.flContainer.addView(mapView)
    }

    private fun initBtnListener(binding: ActivityMainBinding) {
        binding.fabTracking.setOnClickListener { viewModel.toggleTrackingMode() }
        binding.fabList.setOnClickListener { viewModel.toggleListMode() }
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

    private fun observeViewModel(
        binding: ActivityMainBinding, bottomSheetBehavior: BottomSheetBehavior<ComposeView>
    ) {
        viewModel.mapViewModeLiveData.observe(this) {
            binding.fabList.isVisible = it.isNotDefault
            binding.cvBottomSheet.isVisible = it.isNotDefault
            if (it.isDefault) {
                mapView!!.removeAllPOIItems()
            }
        }
        viewModel.trackingModeLiveData.observe(this) {
            when (it) {
                CurrentLocationTrackingMode.TrackingModeOff -> binding.fabTracking.setImageResource(
                    R.drawable.baseline_gps_fixed_24
                )
                CurrentLocationTrackingMode.TrackingModeOnWithoutHeading -> binding.fabTracking.setImageResource(
                    R.drawable.baseline_gps_activated_24
                )
                CurrentLocationTrackingMode.TrackingModeOnWithHeading -> binding.fabTracking.setImageResource(
                    R.drawable.baseline_compass_calibration_24
                )
                else -> {}
            }
            if (it.isEnabled()) {
                AccessFineLocationUtil.checkPermission(this, {
                    mapView!!.currentLocationTrackingMode = it
                }, {
                    viewModel.tempTrackingModeForEnable = it
                }) {
                    showToast(R.string.Toast_location_permission_denied)
                }
            } else {
                mapView!!.currentLocationTrackingMode = it
            }
        }
        viewModel.listModeLiveData.observe(this) {
            when (it) {
                ListMode.LIST -> binding.fabList.setImageResource(R.drawable.baseline_map_24)
                ListMode.MAP -> binding.fabList.setImageResource(R.drawable.baseline_list_24)
                else -> {}
            }
            bottomSheetBehavior.setVisible(it.isList)
        }
        viewModel.documentResultEvent.observe(this) {
            mapView!!.removeAllPOIItems()
            val documentList = it!!.documentList
            val mapPointList: MutableList<MapPoint> = ArrayList()
            documentList.forEach { document ->
                val mapPoint = MapPoint.mapPointWithGeoCoord(document.y(), document.x())
                mapPointList.add(mapPoint)
                val poiItem = addPOIItemInMapView(
                    mapPoint,
                    document.placeName + String.format(Locale.getDefault(), " %.1f", document.rate)
                )
                document.mapPOIItem = poiItem
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
        binding.flContainer.removeView(mapView)
        mapView = null
        super.onDestroy()
    }
}
