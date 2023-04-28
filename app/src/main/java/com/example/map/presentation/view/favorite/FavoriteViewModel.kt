package com.example.map.presentation.view.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.map.MyApplication
import com.example.map.base.BaseViewModel
import com.example.map.data.local.database.AppDatabase
import com.example.map.data.repositoryimpl.FavoriteDocumentRepositoryImpl
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.presentation.model.Document
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteViewModel(appDatabase: AppDatabase) : BaseViewModel() {
    private val favoriteDocumentRepository: FavoriteDocumentRepository
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
        favoriteDocumentRepository =
            FavoriteDocumentRepositoryImpl(appDatabase.favoriteDocumentDao())
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApplication)
                FavoriteViewModel(AppDatabase.getInstance(app.applicationContext))
            }
        }
    }
}