package com.example.map.presentation.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.map.R
import com.example.map.databinding.ActivityMainBinding
import com.example.map.extension.isEnabled
import com.example.map.extension.setVisible
import com.example.map.extension.showToast
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.view.favorite.FavoriteActivity
import com.example.map.presentation.view.main.adapter.DocumentAdapter
import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder
import com.example.map.presentation.view.main.entity.ListMode
import com.example.map.presentation.view.main.entity.MapViewMode
import com.example.map.presentation.view.main.entity.SearchType
import com.example.map.presentation.view.main.mapview.BasicMapViewEventListener
import com.example.map.presentation.view.main.mapview.BasicPOIItemEventListener
import com.example.map.presentation.view.search.SearchActivity
import com.example.map.util.AccessFineLocationUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.*
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mapView: MapView? = null
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
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
            binding.rvDocument.scrollToPosition(index)
            viewModel.selectDocument(index)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val documentAdapter = DocumentAdapter(createOnClickListener())
        initView(binding, documentAdapter)
        observeViewModel(binding, documentAdapter)
    }

    private fun createOnClickListener(): DocumentViewHolder.OnClickListener {
        return object : DocumentViewHolder.OnClickListener {
            override fun onClick(document: Document, position: Int) {
                viewModel.selectDocument(position)
                mapView!!.selectPOIItem(document.mapPOIItem, true)
                mapView!!.moveCamera(CameraUpdateFactory.newMapPoint(document.mapPOIItem!!.mapPoint))
            }

            override fun onFavoriteClick(document: Document) {
                viewModel.addFavoriteDocument(document)
            }

            override fun onUnFavoriteClick(document: Document) {
                viewModel.removeFavoriteDocument(document)
            }
        }
    }

    private fun initView(binding: ActivityMainBinding, documentAdapter: DocumentAdapter) {
        initMapView(binding)
        initBtnListener(binding)
        initBottomSheet(binding, documentAdapter)
    }

    private fun initMapView(binding: ActivityMainBinding) {
        mapView = MapView(this).apply {
            setMapViewEventListener(this@MainActivity.mapViewEventListener)
            setPOIItemEventListener(poiItemEventListener)
        }
        binding.flContainer.addView(mapView)
    }

    private fun initBtnListener(binding: ActivityMainBinding) {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnSearchBar.setOnClickListener { startSearchActivity() }
        binding.btnFood.setOnClickListener { requestSearch(SearchType.FOOD, true) }
        binding.btnCafe.setOnClickListener { requestSearch(SearchType.CAFE, true) }
        binding.btnConvenience.setOnClickListener {
            requestSearch(SearchType.CONVENIENCE, true)
        }
        binding.btnFlower.setOnClickListener { requestSearch(SearchType.FLOWER, true) }
        binding.btnFavorite.setOnClickListener { startActivity(FavoriteActivity::class.java) }
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

    private fun initBottomSheet(binding: ActivityMainBinding, documentAdapter: DocumentAdapter) {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.clBottomSheet).apply {
            isDraggable = false
        }
        binding.rvDocument.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.rvDocument.adapter = documentAdapter
        binding.rvDocument.itemAnimator = null
    }

    private fun observeViewModel(binding: ActivityMainBinding, documentAdapter: DocumentAdapter) {
        viewModel.mapViewModeLiveData.observe(this) {
            binding.btnBack.isEnabled = it.isNotDefault
            binding.btnSearchBar.isEnabled = it.isDefault
            binding.btnFood.isSelected = it == MapViewMode.SEARCH_FOOD
            binding.btnCafe.isSelected = it == MapViewMode.SEARCH_CAFE
            binding.btnConvenience.isSelected = it == MapViewMode.SEARCH_CONVENIENCE
            binding.btnFlower.isSelected = it == MapViewMode.SEARCH_FLOWER
            binding.fabList.isVisible = it.isNotDefault
            binding.clBottomSheet.isVisible = it.isNotDefault
            when (it) {
                MapViewMode.DEFAULT -> {
                    binding.btnSearchBar.setText(R.string.MainActivity_search_bar)
                    mapView!!.removeAllPOIItems()
                }
                MapViewMode.SEARCH_FOOD -> binding.btnSearchBar.setText(R.string.MainActivity_food)
                MapViewMode.SEARCH_CAFE -> binding.btnSearchBar.setText(R.string.MainActivity_cafe)
                MapViewMode.SEARCH_CONVENIENCE -> binding.btnSearchBar.setText(R.string.MainActivity_convenience)
                MapViewMode.SEARCH_FLOWER -> binding.btnSearchBar.setText(R.string.MainActivity_flower)
                else -> {}
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
            bottomSheetBehavior!!.setVisible(it.isList)
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
            documentAdapter.setItemList(documentList)
        }
        viewModel.mainActionEvent.observe(this) {
            when (it) {
                MainAction.SEARCH_FAILURE -> showToast(R.string.Toast_search_failure)
                MainAction.EMPTY_SEARCH -> showToast(R.string.Toast_empty_search)
                MainAction.EMPTY_FAVORITE -> showToast(R.string.Toast_empty_favorite)
                else -> {}
            }
        }
        viewModel.selectPositionEvent.observe(this) {
            documentAdapter.updateSelect(it!!.oldPosition, it.position)
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
        viewModel.onDestroy()
        super.onDestroy()
    }
}
