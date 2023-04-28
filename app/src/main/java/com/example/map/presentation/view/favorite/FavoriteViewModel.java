package com.example.map.presentation.view.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.map.MyApplication;
import com.example.map.base.BaseViewModel;
import com.example.map.data.local.database.AppDatabase;
import com.example.map.data.repositoryimpl.FavoriteDocumentRepositoryImpl;
import com.example.map.domain.repository.FavoriteDocumentRepository;
import com.example.map.presentation.model.Document;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;

public class FavoriteViewModel extends BaseViewModel {
    private final SavedStateHandle handle;
    private final FavoriteDocumentRepository favoriteDocumentRepository;
    public MutableLiveData<List<Document>> favoriteDocumentListLiveData = new MutableLiveData<>();

    public FavoriteViewModel(SavedStateHandle handle, @NonNull AppDatabase appDatabase) {
        this.handle = handle;
        favoriteDocumentRepository = new FavoriteDocumentRepositoryImpl(appDatabase.favoriteDocumentDao());
        compositeDisposable.add(favoriteDocumentRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(documentEntities -> {
                List<Document> favoriteDocumentList = CollectionsKt.map(documentEntities, Document::fromDocumentEntity);
                favoriteDocumentListLiveData.setValue(favoriteDocumentList);
            }, throwable -> {
                // do nothing
            })
        );
    }

    public void addFavoriteDocument(@NonNull Document document) {
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

    public void removeFavoriteDocument(@NonNull Document document) {
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

    public static final ViewModelInitializer<FavoriteViewModel> initializer = new ViewModelInitializer<>(
        FavoriteViewModel.class,
        creationExtras -> {
            MyApplication app = (MyApplication) creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
            assert app != null;
            SavedStateHandle savedStateHandle = SavedStateHandleSupport.createSavedStateHandle(creationExtras);
            return new FavoriteViewModel(savedStateHandle, AppDatabase.getInstance(app.getApplicationContext()));
        }
    );
}
