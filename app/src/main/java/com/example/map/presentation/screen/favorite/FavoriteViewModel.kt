package com.example.map.presentation.screen.favorite

import androidx.lifecycle.viewModelScope
import com.example.map.base.BaseViewModel
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.presentation.model.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDocumentRepository: FavoriteDocumentRepository
) : BaseViewModel() {
    var favoriteDocumentListFlow = favoriteDocumentRepository.getAll().distinctUntilChanged().map {
        it.map { entity -> Document.fromDocumentEntity(entity) }
    }

    fun addFavoriteDocument(document: Document) = viewModelScope.launch {
        favoriteDocumentRepository.insert(document.toEntity())
    }

    fun removeFavoriteDocument(document: Document) = viewModelScope.launch {
        favoriteDocumentRepository.delete(document.toEntity())
    }
}
