package com.example.map.di

import android.content.Context
import androidx.room.Room
import com.example.map.data.local.dao.FavoriteDocumentDao
import com.example.map.data.local.database.MapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMapDatabase(
        @ApplicationContext context: Context
    ): MapDatabase {
        return Room.databaseBuilder(context, MapDatabase::class.java, "app-database").build()
    }

    @Provides
    fun provideFavoriteDocumentDao(appDatabase: MapDatabase): FavoriteDocumentDao {
        return appDatabase.favoriteDocumentDao()
    }
}
