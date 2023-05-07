package com.example.map.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.map.data.local.dao.FavoriteDocumentDao
import com.example.map.data.local.model.DocumentEntity

@Database(entities = [DocumentEntity::class], version = 1)
abstract class MapDatabase : RoomDatabase() {
    abstract fun favoriteDocumentDao(): FavoriteDocumentDao
}
