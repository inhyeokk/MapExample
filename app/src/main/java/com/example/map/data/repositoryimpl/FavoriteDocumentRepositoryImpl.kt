package com.example.map.data.repositoryimpl

import com.example.map.data.local.dao.FavoriteDocumentDao
import com.example.map.data.local.model.DocumentEntity
import com.example.map.domain.repository.FavoriteDocumentRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

@ViewModelScoped
class FavoriteDocumentRepositoryImpl @Inject constructor(
    private val dao: FavoriteDocumentDao
) : FavoriteDocumentRepository {
    override fun getAll(): Flowable<List<DocumentEntity>> {
        return dao.getAll()
    }

    override fun get(id: String): Flowable<DocumentEntity> {
        return dao.get(id)
    }

    override fun insert(documentEntity: DocumentEntity): Completable {
        return dao.insert(documentEntity)
    }

    override fun delete(documentEntity: DocumentEntity): Completable {
        return dao.delete(documentEntity)
    }
}
