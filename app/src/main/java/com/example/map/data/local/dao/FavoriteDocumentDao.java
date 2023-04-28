package com.example.map.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.map.data.local.model.DocumentEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavoriteDocumentDao {
    @Query("SELECT * FROM documententity")
    Flowable<List<DocumentEntity>> getAll();

    @Query("SELECT * FROM documententity WHERE id =:id")
    Flowable<DocumentEntity> get(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DocumentEntity documentEntity);

    @Delete
    Completable delete(DocumentEntity documentEntity);

}
