package com.example.map.domain.repository

import com.example.map.data.local.model.DocumentEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface FavoriteDocumentRepository {
    fun getAll(): Flowable<List<DocumentEntity>>
    fun get(id: String): Flowable<DocumentEntity>
    fun insert(documentEntity: DocumentEntity): Completable
    fun delete(documentEntity: DocumentEntity): Completable
}