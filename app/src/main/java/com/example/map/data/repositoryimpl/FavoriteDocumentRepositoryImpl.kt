package com.example.map.data.repositoryimpl

import com.example.map.data.local.dao.FavoriteDocumentDao
import com.example.map.data.local.model.DocumentEntity
import com.example.map.domain.repository.FavoriteDocumentRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class FavoriteDocumentRepositoryImpl @Inject constructor(
    private val dao: FavoriteDocumentDao
) : FavoriteDocumentRepository {
    override fun getAll(): Flow<List<DocumentEntity>> {
        return dao.getAll()
    }

    override fun get(id: String): Flow<DocumentEntity> {
        return dao.get(id)
    }

    override suspend fun insert(documentEntity: DocumentEntity) {
        dao.insert(documentEntity)
    }

    override suspend fun delete(documentEntity: DocumentEntity) {
        dao.delete(documentEntity)
    }
}
