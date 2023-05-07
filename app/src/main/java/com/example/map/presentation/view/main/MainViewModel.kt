package com.example.map.presentation.view.main

import androidx.lifecycle.*
import com.example.map.SingleLiveEvent
import com.example.map.base.BaseViewModel
import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.repository.MapDataStoreRepository
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.DocumentResult
import com.example.map.presentation.view.main.entity.ListMode
import com.example.map.presentation.view.main.entity.MapViewMode
import com.example.map.presentation.view.main.entity.SearchType
import com.example.map.presentation.view.main.entity.SelectPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val mapDataStoreRepository: MapDataStoreRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val favoriteDocumentRepository: FavoriteDocumentRepository
) : BaseViewModel() {
    val mapViewModeLiveData = handle.getLiveData(MAP_VIEW_MODE, MapViewMode.DEFAULT)
    val trackingModeLiveData = mapDataStoreRepository.getTrackingMode().asLiveData()
    val listModeLiveData = handle.getLiveData(LIST_MODE, ListMode.LIST)
    private var _documentResultEvent = MutableLiveData<DocumentResult>()
    val documentResultEvent: LiveData<DocumentResult> = _documentResultEvent
    private val favoriteDocumentListCache: MutableList<Document> = ArrayList()
    var mainActionEvent = SingleLiveEvent<MainAction?>()
    var selectPositionEvent = MutableLiveData<SelectPosition>()
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
            trackingMode = CurrentLocationTrackingMode.TrackingModeOff
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

    private fun setTrackingMode(trackingMode: CurrentLocationTrackingMode) = viewModelScope.launch {
        mapDataStoreRepository.setTrackingMode(trackingMode)
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

    fun search(
        searchType: SearchType, x: String, y: String, isMoveCamera: Boolean
    ) = viewModelScope.launch {
        if (searchType.isCategory) {
            val request = SearchByCategoryRequest(searchType.type, x, y, 5000)
            localSearchRepository.searchByCategory(request).onSuccess {
                handleSearchResult(searchType, it, isMoveCamera)
            }.onFailure {
                setFailureOrEmpty(MainAction.SEARCH_FAILURE)
            }
        } else { // keyword
            val request = SearchByKeywordRequest(searchType.type, x, y, 5000)
            localSearchRepository.searchByKeyword(request).onSuccess {
                handleSearchResult(searchType, it, isMoveCamera)
            }.onFailure {
                setFailureOrEmpty(MainAction.SEARCH_FAILURE)
            }
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
        val newDocumentList = documentList.map {
            val favoriteDocument = favoriteDocumentList.find { document1 -> document1.id == it.id }
            it.copy(isFavorite = favoriteDocument != null)
        }
        _documentResultEvent.value = DocumentResult(newDocumentList, isMoveCamera)
    }

    private fun setFailureOrEmpty(mainAction: MainAction) {
        mapViewMode = MapViewMode.DEFAULT
        mainActionEvent.value = mainAction
    }

    fun addFavoriteDocument(document: Document) = viewModelScope.launch {
        favoriteDocumentRepository.insert(document.toEntity())
    }

    fun removeFavoriteDocument(document: Document) = viewModelScope.launch {
        favoriteDocumentRepository.delete(document.toEntity())
    }

    fun selectDocument(position: Int, selectedByMap: Boolean) {
        selectPositionEvent.value = SelectPosition(position, selectedByMap)
    }

    private fun clearSelect() {
        selectPositionEvent.value = SelectPosition()
    }

    init {
        viewModelScope.launch {
            favoriteDocumentRepository.getAll().map {
                it.map { entity ->
                    Document.fromDocumentEntity(entity)
                }
            }.collectLatest {
                favoriteDocumentListCache.clear()
                favoriteDocumentListCache.addAll(it)
                val result = documentResultEvent.value
                if (result != null) {
                    setDocumentListWithFavorite(
                        result.documentList, it, false
                    )
                }
            }
        }
    }

    companion object {
        private const val MAP_VIEW_MODE = "MAP_VIEW_MODE"
        private const val LIST_MODE = "LIST_MODE"
        private const val TEMP_TRACKING_MODE = "TEMP_TRACKING_MODE"
    }
}
