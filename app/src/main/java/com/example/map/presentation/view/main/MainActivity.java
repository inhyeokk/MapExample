package com.example.map.presentation.view.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.map.R;
import com.example.map.databinding.ActivityMainBinding;
import com.example.map.extension.BottomSheetBehaviorKt;
import com.example.map.extension.CurrentLocationTrackingModeKt;
import com.example.map.extension.ListKt;
import com.example.map.extension.ToastKt;
import com.example.map.presentation.model.Document;
import com.example.map.presentation.model.SearchResult;
import com.example.map.presentation.view.favorite.FavoriteActivity;
import com.example.map.presentation.view.main.adapter.DocumentAdapter;
import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder;
import com.example.map.presentation.view.main.entity.MapViewMode;
import com.example.map.presentation.view.main.entity.SearchType;
import com.example.map.presentation.view.main.mapview.BasicMapViewEventListener;
import com.example.map.presentation.view.main.mapview.BasicPOIItemEventListener;
import com.example.map.presentation.view.search.SearchActivity;
import com.example.map.util.AccessFineLocationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MapView mapView;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private MainViewModel viewModel;
    private final ActivityResultLauncher<Intent> searchResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().hasExtra(SearchActivity.SEARCH_RESULT)) {
            setSearchResultInMapView((SearchResult) result.getData().getSerializableExtra(SearchActivity.SEARCH_RESULT));
        }
    });

    private void setSearchResultInMapView(@NonNull SearchResult searchResult) {
        mapView.removeAllPOIItems();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(searchResult.getY(), searchResult.getX());
        addPOIItemInMapView(mapPoint, searchResult.getAddressName());
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint));
    }

    private final MapView.MapViewEventListener mapViewEventListener = new BasicMapViewEventListener() {
        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
            viewModel.disableTrackingMode();
        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
            SearchType searchType = SearchType.from(viewModel.getMapViewMode());
            if (searchType != null) {
                requestSearch(searchType, false);
            }
        }
    };
    private final MapView.POIItemEventListener poiItemEventListener = new BasicPOIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            if (viewModel.getMapViewMode().isNotDefault()) {
                selectDocumentBy(mapPOIItem);
            }
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPOIItem.getMapPoint()));
        }
    };

    private void selectDocumentBy(MapPOIItem mapPOIItem) {
        List<Document> documentList = viewModel.requireDocumentList();
        Document document = ListKt.find(documentList, document1 -> document1.getMapPOIItem().equals(mapPOIItem));
        if (document != null) {
            int index = documentList.indexOf(document);
            binding.rvDocument.scrollToPosition(index);
            viewModel.selectDocument(index);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DocumentAdapter documentAdapter = new DocumentAdapter(createOnClickListener());
        initView(binding, documentAdapter);
        observeViewModel(binding, documentAdapter);
    }

    @NonNull
    private DocumentViewHolder.OnClickListener createOnClickListener() {
        return new DocumentViewHolder.OnClickListener() {
            @Override
            public void onClick(Document document, int position) {
                viewModel.selectDocument(position);
                mapView.selectPOIItem(document.getMapPOIItem(), true);
                mapView.moveCamera(CameraUpdateFactory.newMapPoint(document.getMapPOIItem().getMapPoint()));
            }

            @Override
            public void onFavoriteClick(Document document) {
                viewModel.addFavoriteDocument(document);
            }

            @Override
            public void onUnFavoriteClick(Document document) {
                viewModel.removeFavoriteDocument(document);
            }
        };
    }

    private void initView(ActivityMainBinding binding, DocumentAdapter documentAdapter) {
        initMapView(binding);
        initBtnListener(binding);
        initBottomSheet(binding, documentAdapter);
    }

    private void initMapView(ActivityMainBinding binding) {
        mapView = new MapView(this);
        mapView.setMapViewEventListener(mapViewEventListener);
        mapView.setPOIItemEventListener(poiItemEventListener);
        binding.flContainer.addView(mapView);
    }

    private void initBtnListener(ActivityMainBinding binding) {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnSearchBar.setOnClickListener(v -> startSearchActivity());
        binding.btnFood.setOnClickListener(v -> requestSearch(SearchType.FOOD, true));
        binding.btnCafe.setOnClickListener(v -> requestSearch(SearchType.CAFE, true));
        binding.btnConvenience.setOnClickListener(v -> requestSearch(SearchType.CONVENIENCE, true));
        binding.btnFlower.setOnClickListener(v -> requestSearch(SearchType.FLOWER, true));
        binding.btnFavorite.setOnClickListener(v -> startActivity(FavoriteActivity.class));
        binding.fabTracking.setOnClickListener(v -> viewModel.toggleTrackingMode());
        binding.fabList.setOnClickListener(v -> viewModel.toggleListMode());
    }

    private void startSearchActivity() {
        viewModel.disableTrackingMode();
        Intent intent = new Intent(this, SearchActivity.class);
        searchResultLauncher.launch(intent);
    }

    private void requestSearch(SearchType searchType, boolean isMoveCamera) {
        MapPoint.GeoCoordinate mapPoint = mapView.getMapCenterPoint().getMapPointGeoCoord();
        viewModel.search(searchType, String.valueOf(mapPoint.longitude), String.valueOf(mapPoint.latitude), isMoveCamera);
    }

    private void startActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private void initBottomSheet(@NonNull ActivityMainBinding binding, DocumentAdapter documentAdapter) {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.clBottomSheet);
        bottomSheetBehavior.setDraggable(false);
        binding.rvDocument.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvDocument.setAdapter(documentAdapter);
        binding.rvDocument.setItemAnimator(null);
    }

    private void observeViewModel(ActivityMainBinding binding, DocumentAdapter documentAdapter) {
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(MainViewModel.initializer)).get(MainViewModel.class);
        viewModel.mapViewModeLiveData.observe(this, mapViewMode -> {
            binding.btnBack.setEnabled(mapViewMode.isNotDefault());
            binding.btnSearchBar.setEnabled(mapViewMode.isDefault());
            binding.btnFood.setSelected(mapViewMode == MapViewMode.SEARCH_FOOD);
            binding.btnCafe.setSelected(mapViewMode == MapViewMode.SEARCH_CAFE);
            binding.btnConvenience.setSelected(mapViewMode == MapViewMode.SEARCH_CONVENIENCE);
            binding.btnFlower.setSelected(mapViewMode == MapViewMode.SEARCH_FLOWER);
            ViewKt.setVisible(binding.fabList, mapViewMode.isNotDefault());
            ViewKt.setVisible(binding.clBottomSheet, mapViewMode.isNotDefault());
            switch (mapViewMode) {
                case DEFAULT:
                    binding.btnSearchBar.setText(R.string.MainActivity_search_bar);
                    mapView.removeAllPOIItems();
                    break;
                case SEARCH_FOOD:
                    binding.btnSearchBar.setText(R.string.MainActivity_food);
                    break;
                case SEARCH_CAFE:
                    binding.btnSearchBar.setText(R.string.MainActivity_cafe);
                    break;
                case SEARCH_CONVENIENCE:
                    binding.btnSearchBar.setText(R.string.MainActivity_convenience);
                    break;
                case SEARCH_FLOWER:
                    binding.btnSearchBar.setText(R.string.MainActivity_flower);
                    break;
            }
        });
        viewModel.trackingModeLiveData.observe(this, trackingMode -> {
            switch (trackingMode) {
                case TrackingModeOff:
                    binding.fabTracking.setImageResource(R.drawable.baseline_gps_fixed_24);
                    break;
                case TrackingModeOnWithoutHeading:
                    binding.fabTracking.setImageResource(R.drawable.baseline_gps_activated_24);
                    break;
                case TrackingModeOnWithHeading:
                    binding.fabTracking.setImageResource(R.drawable.baseline_compass_calibration_24);
                    break;
            }
            if (CurrentLocationTrackingModeKt.isEnabled(trackingMode)) {
                AccessFineLocationUtil.checkPermission(this, () -> {
                    mapView.setCurrentLocationTrackingMode(trackingMode);
                }, () -> {
                    viewModel.setTempTrackingModeForEnable(trackingMode);
                }, () -> {
                    ToastKt.show(this, R.string.Toast_location_permission_denied);
                });
            } else {
                mapView.setCurrentLocationTrackingMode(trackingMode);
            }
        });
        viewModel.listModeLiveData.observe(this, listMode -> {
            switch (listMode) {
                case LIST:
                    binding.fabList.setImageResource(R.drawable.baseline_map_24);
                    break;
                case MAP:
                    binding.fabList.setImageResource(R.drawable.baseline_list_24);
                    break;
            }
            BottomSheetBehaviorKt.setVisible(bottomSheetBehavior, listMode.isList());
        });
        viewModel.documentResultEvent.observe(this, documentResult -> {
            mapView.removeAllPOIItems();
            List<Document> documentList = documentResult.getDocumentList();
            List<MapPoint> mapPointList = new ArrayList<>();
            documentList.forEach(document -> {
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(document.getY(), document.getX());
                mapPointList.add(mapPoint);
                MapPOIItem poiItem = addPOIItemInMapView(mapPoint, document.getPlaceName() + String.format(Locale.getDefault(), " %.1f", document.getRate()));
                document.setMapPOIItem(poiItem);
            });
            if (documentResult.isMoveCamera()) {
                MapPointBounds mapPointBounds = new MapPointBounds(mapPointList.toArray(new MapPoint[0]));
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, 100));
            }
            documentAdapter.setItemList(documentList);
        });
        viewModel.mainActionEvent.observe(this, mainAction -> {
            switch (mainAction) {
                case SEARCH_FAILURE:
                    ToastKt.show(this, R.string.Toast_search_failure);
                    break;
                case EMPTY_SEARCH:
                    ToastKt.show(this, R.string.Toast_empty_search);
                    break;
                case EMPTY_FAVORITE:
                    ToastKt.show(this, R.string.Toast_empty_favorite);
                    break;
            }
        });
        viewModel.selectPositionEvent.observe(this, selectPosition -> {
            documentAdapter.updateSelect(selectPosition.getOldPosition(), selectPosition.getPosition());
        });
    }

    private MapPOIItem addPOIItemInMapView(MapPoint mapPoint, String name) {
        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName(name);
        poiItem.setShowDisclosureButtonOnCalloutBalloon(false);
        poiItem.setMapPoint(mapPoint);
        poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(poiItem);
        return poiItem;
    }

    @Override
    public void onBackPressed() {
        if (viewModel.getMapViewMode().isNotDefault()) {
            viewModel.setMapViewMode(MapViewMode.DEFAULT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AccessFineLocationUtil.onRequestPermissionsResult(requestCode, grantResults, () -> {
            viewModel.enableTrackingMode();
        }, () -> {
            viewModel.disableTrackingMode();
            ToastKt.show(this, R.string.Toast_location_permission_denied);
        });
    }

    @Override
    protected void onDestroy() {
        binding.flContainer.removeView(mapView);
        mapView = null;
        viewModel.onDestroy();
        super.onDestroy();
    }
}
