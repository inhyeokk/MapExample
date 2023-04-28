package com.example.map.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.map.data.local.dao.FavoriteDocumentDao
import com.example.map.data.local.model.DocumentEntity

@Database(entities = [DocumentEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDocumentDao(): FavoriteDocumentDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build()
        }
    }
}
