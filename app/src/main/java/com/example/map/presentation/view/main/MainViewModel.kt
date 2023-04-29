package com.example.map.presentation.view.main

import androidx.lifecycle.SavedStateHandle
import com.example.map.SingleLiveEvent
import com.example.map.base.BaseViewModel
import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.DocumentResult
import com.example.map.presentation.view.main.entity.ListMode
import com.example.map.presentation.view.main.entity.MapViewMode
import com.example.map.presentation.view.main.entity.SearchType
import com.example.map.presentation.view.main.entity.SelectPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val localSearchRepository: LocalSearchRepository,
    private val favoriteDocumentRepository: FavoriteDocumentRepository
) : BaseViewModel() {
    val mapViewModeLiveData = handle.getLiveData(MAP_VIEW_MODE, MapViewMode.DEFAULT)
    val trackingModeLiveData = handle.getLiveData(TRACKING_MODE, CurrentLocationTrackingMode.TrackingModeOnWithoutHeading)
    val listModeLiveData = handle.getLiveData(LIST_MODE, ListMode.LIST)
    var documentResultEvent = SingleLiveEvent<DocumentResult>()
    private val favoriteDocumentListCache: MutableList<Document> = ArrayList()
    var mainActionEvent = SingleLiveEvent<MainAction?>()
    var selectPositionEvent = SingleLiveEvent<SelectPosition>()
    var mapViewMode: MapViewMode
        get() = mapViewModeLiveData.value!!
        set(mapViewMode) {
            if (mapViewMode.isNotDefault) {
                disableTrackingMode()
                listMode = ListMode.LIST
            }
            mapViewModeLiveData.value = mapViewMode
        }

    fun toggleTrackingMode() {
        var trackingMode = trackingModeLiveData.value
        if (trackingMode == null) {
            trackingMode = CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        }
        val newTrackingMode = when (trackingMode) {
            CurrentLocationTrackingMode.TrackingModeOnWithoutHeading -> CurrentLocationTrackingMode.TrackingModeOnWithHeading
            CurrentLocationTrackingMode.TrackingModeOnWithHeading -> CurrentLocationTrackingMode.TrackingModeOff
            else -> CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        }
        setTrackingMode(newTrackingMode)
    }

    fun enableTrackingMode() {
        setTrackingMode(tempTrackingModeForEnable!!)
    }

    fun disableTrackingMode() {
        setTrackingMode(CurrentLocationTrackingMode.TrackingModeOff)
    }

    private fun setTrackingMode(trackingMode: CurrentLocationTrackingMode) {
        trackingModeLiveData.value = trackingMode
    }

    var tempTrackingModeForEnable: CurrentLocationTrackingMode?
        get() {
            val tempTrackingMode = handle.remove<CurrentLocationTrackingMode>(TEMP_TRACKING_MODE)
            return tempTrackingMode ?: CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        }
        set(trackingMode) {
            handle[TEMP_TRACKING_MODE] = trackingMode
        }
    var listMode: ListMode
        get() = listModeLiveData.value!!
        set(listMode) {
            listModeLiveData.value = listMode
        }

    fun toggleListMode() {
        val newListMode = if (listMode == ListMode.LIST) ListMode.MAP else ListMode.LIST
        this.listMode = newListMode
    }

    val requireDocumentList get() = documentResultEvent.value!!.documentList

    fun search(searchType: SearchType, x: String, y: String, isMoveCamera: Boolean) {
        if (searchType.isCategory) {
            val request = SearchByCategoryRequest(searchType.type, x, y, 5000)
            localSearchRepository.searchByCategory(request, {
                handleSearchResult(searchType, it, isMoveCamera)
            }) { setFailureOrEmpty(MainAction.SEARCH_FAILURE) }
        } else { // keyword
            val request = SearchByKeywordRequest(searchType.type, x, y, 5000)
            localSearchRepository.searchByKeyword(request, {
                handleSearchResult(searchType, it, isMoveCamera)
            }) { setFailureOrEmpty(MainAction.SEARCH_FAILURE) }
        }
    }

    private fun handleSearchResult(
        searchType: SearchType, result: LocalSearchResult, isMoveCamera: Boolean
    ) {
        if (result.documents.isNotEmpty()) {
            mapViewMode = searchType.toMapViewMode()
            clearSelect()
            val documentList: List<Document> = result.documents.map {
                Document.fromDocumentResult(it)
            }
            setDocumentListWithFavorite(documentList, favoriteDocumentListCache, isMoveCamera)
        } else {
            setFailureOrEmpty(MainAction.EMPTY_SEARCH)
        }
    }

    private fun setDocumentListWithFavorite(
        documentList: List<Document>, favoriteDocumentList: List<Document>, isMoveCamera: Boolean
    ) {
        documentList.forEach {
            val favoriteDocument = favoriteDocumentList.find { document1 -> document1.id == it.id }
            it.isFavorite = favoriteDocument != null
        }
        documentResultEvent.value = DocumentResult(documentList, isMoveCamera)
    }

    private fun setFailureOrEmpty(mainAction: MainAction) {
        mapViewMode = MapViewMode.DEFAULT
        mainActionEvent.value = mainAction
    }

    fun addFavoriteDocument(document: Document) {
        compositeDisposable.add(favoriteDocumentRepository.insert(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) { })
    }

    fun removeFavoriteDocument(document: Document) {
        compositeDisposable.add(favoriteDocumentRepository.delete(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) { })
    }

    fun selectDocument(position: Int) {
        selectPositionEvent.value = selectPositionEvent.value?.let {
            SelectPosition(it.position, position)
        } ?: SelectPosition(position)
    }

    private fun clearSelect() {
        selectPositionEvent.value = SelectPosition()
    }

    init {
        compositeDisposable.add(favoriteDocumentRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val favoriteDocumentList = it.map { entity ->
                    Document.fromDocumentEntity(entity)
                }
                favoriteDocumentListCache.clear()
                favoriteDocumentListCache.addAll(favoriteDocumentList)
                val result = documentResultEvent.value
                if (result != null) {
                    setDocumentListWithFavorite(
                        result.documentList, favoriteDocumentList, false
                    )
                }
            }) {

            })
    }

    companion object {
        private const val MAP_VIEW_MODE = "MAP_VIEW_MODE"
        private const val LIST_MODE = "LIST_MODE"
        private const val TRACKING_MODE = "TRACKING_MODE"
        private const val TEMP_TRACKING_MODE = "TEMP_TRACKING_MODE"
    }
}
