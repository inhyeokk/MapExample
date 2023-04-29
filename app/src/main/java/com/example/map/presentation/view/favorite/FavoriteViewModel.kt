package com.example.map.presentation.view.favorite

import androidx.lifecycle.MutableLiveData
import com.example.map.base.BaseViewModel
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.presentation.model.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDocumentRepository: FavoriteDocumentRepository
) : BaseViewModel() {
    var favoriteDocumentListLiveData = MutableLiveData<List<Document>>()
    fun addFavoriteDocument(document: Document) {
        compositeDisposable.add(favoriteDocumentRepository.insert(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) { }
        )
    }

    fun removeFavoriteDocument(document: Document) {
        compositeDisposable.add(favoriteDocumentRepository.delete(document.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) { }
        )
    }

    init {
        compositeDisposable.add(favoriteDocumentRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val favoriteDocumentList = it.map { entity ->
                    Document.fromDocumentEntity(entity)
                }
                favoriteDocumentListLiveData.setValue(favoriteDocumentList)
            }) { }
        )
    }

}
