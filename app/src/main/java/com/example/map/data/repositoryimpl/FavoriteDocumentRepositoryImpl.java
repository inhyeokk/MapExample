package com.example.map.data.repositoryimpl;

import com.example.map.data.local.dao.FavoriteDocumentDao;
import com.example.map.data.local.model.DocumentEntity;
import com.example.map.domain.repository.FavoriteDocumentRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FavoriteDocumentRepositoryImpl implements FavoriteDocumentRepository {
    private final FavoriteDocumentDao dao;

    public FavoriteDocumentRepositoryImpl(FavoriteDocumentDao dao) {
        this.dao = dao;
    }

    @Override
    public Flowable<List<DocumentEntity>> getAll() {
        return dao.getAll();
    }

    @Override
    public Flowable<DocumentEntity> get(String id) {
        return dao.get(id);
    }

    @Override
    public Completable insert(DocumentEntity documentEntity) {
        return dao.insert(documentEntity);
    }

    @Override
    public Completable delete(DocumentEntity documentEntity) {
        return dao.delete(documentEntity);
    }
}
