package com.example.map.domain.repository;

import com.example.map.data.local.model.DocumentEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface FavoriteDocumentRepository {
    Flowable<List<DocumentEntity>> getAll();
    Flowable<DocumentEntity> get(String id);
    Completable insert(DocumentEntity documentEntity);
    Completable delete(DocumentEntity documentEntity);
}
