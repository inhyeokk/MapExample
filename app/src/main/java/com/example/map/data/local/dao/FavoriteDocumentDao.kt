package com.example.map.data.local.dao

import androidx.room.*
import com.example.map.data.local.model.DocumentEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface FavoriteDocumentDao {
    @Query("SELECT * FROM documententity")
    fun getAll(): Flowable<List<DocumentEntity>>

    @Query("SELECT * FROM documententity WHERE id =:id")
    fun get(id: String): Flowable<DocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(documentEntity: DocumentEntity): Completable

    @Delete
    fun delete(documentEntity: DocumentEntity): Completable
}
