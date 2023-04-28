package com.example.map.presentation.view.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.map.MyApplication;
import com.example.map.SingleLiveEvent;
import com.example.map.base.BaseViewModel;
import com.example.map.data.local.database.AppDatabase;
import com.example.map.data.remote.model.LocalSearchResult;
import com.example.map.data.repositoryimpl.FavoriteDocumentRepositoryImpl;
import com.example.map.data.repositoryimpl.LocalSearchRepositoryImpl;
import com.example.map.domain.repository.FavoriteDocumentRepository;
import com.example.map.domain.repository.LocalSearchRepository;
import com.example.map.domain.request.SearchByCategoryRequest;
import com.example.map.domain.request.SearchByKeywordRequest;
import com.example.map.extension.ListKt;
import com.example.map.presentation.model.Document;
import com.example.map.presentation.model.DocumentResult;
import com.example.map.presentation.view.main.entity.ListMode;
import com.example.map.presentation.view.main.entity.MapViewMode;
import com.example.map.presentation.view.main.entity.SearchType;
import com.example.map.presentation.view.main.entity.SelectPosition;

import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;

public class MainViewModel extends BaseViewModel {
    private final SavedStateHandle handle;
    private final LocalSearchRepository localSearchRepository = new LocalSearchRepositoryImpl();
    private final FavoriteDocumentRepository favoriteDocumentRepository;

    public MutableLiveData<MapViewMode> mapViewModeLiveData;
    public MutableLiveData<CurrentLocationTrackingMode> trackingModeLiveData;
    public MutableLiveData<ListMode> listModeLiveData;
    public SingleLiveEvent<DocumentResult> documentResultEvent = new SingleLiveEvent<>();
    private final List<Document> favoriteDocumentListCache = new ArrayList<>();
    public SingleLiveEvent<MainAction> mainActionEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<SelectPosition> selectPositionEvent = new SingleLiveEvent<>();

    public MainViewModel(SavedStateHandle handle, AppDatabase appDatabase) {
        this.handle = handle;
        favoriteDocumentRepository = new FavoriteDocumentRepositoryImpl(appDatabase.favoriteDocumentDao());
        mapViewModeLiveData = handle.getLiveData(MAP_VIEW_MODE, MapViewMode.DEFAULT);
        trackingModeLiveData = handle.getLiveData(TRACKING_MODE, CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        listModeLiveData = handle.getLiveData(LIST_MODE, ListMode.LIST);
        compositeDisposable.add(favoriteDocumentRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(documentEntities -> {
                List<Document> favoriteDocumentList = CollectionsKt.map(documentEntities, Document::fromDocumentEntity);
                favoriteDocumentListCache.clear();
                favoriteDocumentListCache.addAll(favoriteDocumentList);
                DocumentResult result = documentResultEvent.getValue();
                if (result != null) {
                    setDocumentListWithFavorite(result.getDocumentList(), favoriteDocumentList, false);
                }
            }, throwable -> {
                // do nothing
            })
        );
    }

    public MapViewMode getMapViewMode() {
        return mapViewModeLiveData.getValue();
    }

    public void setMapViewMode(MapViewMode mapViewMode) {
        if (mapViewMode.isNotDefault()) {
            disableTrackingMode();
            setListMode(ListMode.LIST);
        }
        mapViewModeLiveData.setValue(mapViewMode);
    }

    public void toggleTrackingMode() {
        CurrentLocationTrackingMode trackingMode = trackingModeLiveData.getValue();
        if (trackingMode == null) {
            trackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading;
        }
        CurrentLocationTrackingMode newTrackingMode;
        switch (trackingMode) {
            case TrackingModeOnWithoutHeading:
                newTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithHeading;
                break;
            case TrackingModeOnWithHeading:
                newTrackingMode = CurrentLocationTrackingMode.TrackingModeOff;
                break;
            default:
                newTrackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading;
                break;
        }
        setTrackingMode(newTrackingMode);
    }

    public void enableTrackingMode() {
        setTrackingMode(getTempTrackingModeForEnable());
    }

    public void disableTrackingMode() {
        setTrackingMode(CurrentLocationTrackingMode.TrackingModeOff);
    }

    private void setTrackingMode(CurrentLocationTrackingMode trackingMode) {
        trackingModeLiveData.setValue(trackingMode);
    }

    private CurrentLocationTrackingMode getTempTrackingModeForEnable() {
        CurrentLocationTrackingMode tempTrackingMode = handle.remove(TEMP_TRACKING_MODE);
        if (tempTrackingMode != null) {
            return tempTrackingMode;
        } else {
            return CurrentLocationTrackingMode.TrackingModeOnWithoutHeading;
        }
    }

    public void setTempTrackingModeForEnable(CurrentLocationTrackingMode trackingMode) {
        handle.set(TEMP_TRACKING_MODE, trackingMode);
    }

    public ListMode getListMode() {
        return listModeLiveData.getValue();
    }

    public void toggleListMode() {
        ListMode listMode = getListMode();
        if (listMode == null) {
            listMode = ListMode.LIST;
        }
        ListMode newListMode = (listMode == ListMode.LIST) ? ListMode.MAP : ListMode.LIST;
        setListMode(newListMode);
    }

    public void setListMode(ListMode listMode) {
        listModeLiveData.setValue(listMode);
    }

    public List<Document> requireDocumentList() {
        return Objects.requireNonNull(documentResultEvent.getValue()).getDocumentList();
    }

    public void search(SearchType searchType, String x, String y, boolean isMoveCamera) {
        if (searchType.isCategory()) {
            SearchByCategoryRequest request = new SearchByCategoryRequest(searchType.getName(), x, y, 5000);
            localSearchRepository.searchByCategory(request, result -> {
                handleSearchResult(searchType, result, isMoveCamera);
            }, throwable -> {
                setFailureOrEmpty(MainAction.SEARCH_FAILURE);
            });
        } else { // keyword
            SearchByKeywordRequest request = new SearchByKeywordRequest(searchType.getName(), x, y, 5000);
            localSearchRepository.searchByKeyword(request, result -> {
                handleSearchResult(searchType, result, isMoveCamera);
            }, throwable -> {
                setFailureOrEmpty(MainAction.SEARCH_FAILURE);
            });
        }
    }

    private void handleSearchResult(SearchType searchType, LocalSearchResult result, boolean isMoveCamera) {
        if (!result.getDocuments().isEmpty()) {
            setMapViewMode(searchType.toMapViewMode());
            clearSelect();
            List<Document> documentList = CollectionsKt.map(result.getDocuments(), Document::fromDocumentResult);
            setDocumentListWithFavorite(documentList, favoriteDocumentListCache, isMoveCamera);
        } else {
            setFailureOrEmpty(MainAction.EMPTY_SEARCH);
        }
    }

    private void setDocumentListWithFavorite(List<Document> documentList, List<Document> favoriteDocumentList, boolean isMoveCamera) {
        documentList.forEach(document -> {
            Document favoriteDocument = ListKt.find(favoriteDocumentList, document1 -> document1.getId().equals(document.getId()));
            document.setFavorite(favoriteDocument != null);
        });
        documentResultEvent.setValue(new DocumentResult(documentList, isMoveCamera));
    }

    private void setFailureOrEmpty(MainAction mainAction) {
        setMapViewMode(MapViewMode.DEFAULT);
        mainActionEvent.setValue(mainAction);
    }

    public void addFavoriteDocument(Document document) {
        compositeDisposable.add(favoriteDocumentRepository.insert(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                // do nothing
            }, throwable -> {
                // do nothing
            })
        );
    }

    public void removeFavoriteDocument(Document document) {
        compositeDisposable.add(favoriteDocumentRepository.delete(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                // do nothing
            }, throwable -> {
                // do nothing
            })
        );
    }

    public void selectDocument(int position) {
        SelectPosition selectPosition = selectPositionEvent.getValue();
        if (selectPosition == null) {
            selectPosition = new SelectPosition(position);
        } else {
            selectPosition = new SelectPosition(selectPosition.getPosition(), position);
        }
        selectPositionEvent.setValue(selectPosition);
    }

    public void clearSelect() {
        selectPositionEvent.setValue(new SelectPosition());
    }

    private static final String MAP_VIEW_MODE = "MAP_VIEW_MODE";
    private static final String LIST_MODE = "LIST_MODE";
    private static final String TRACKING_MODE = "TRACKING_MODE";
    private static final String TEMP_TRACKING_MODE = "TEMP_TRACKING_MODE";

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
        MainViewModel.class,
        creationExtras -> {
            MyApplication app = (MyApplication) creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
            assert app != null;
            SavedStateHandle savedStateHandle = SavedStateHandleSupport.createSavedStateHandle(creationExtras);
            return new MainViewModel(savedStateHandle, AppDatabase.getInstance(app.getApplicationContext()));
        }
    );
}
