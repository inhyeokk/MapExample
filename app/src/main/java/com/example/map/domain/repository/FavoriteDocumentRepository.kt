package com.example.map.domain.repository

import com.example.map.data.local.model.DocumentEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteDocumentRepository {
    fun getAll(): Flow<List<DocumentEntity>>
    fun get(id: String): Flow<DocumentEntity>
    suspend fun insert(documentEntity: DocumentEntity)
    suspend fun delete(documentEntity: DocumentEntity)
}
