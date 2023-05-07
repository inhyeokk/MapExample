package com.example.map.di

import android.content.Context
import com.example.map.data.local.datastore.MapDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAppDataStore(@ApplicationContext context: Context): MapDataStore {
        return MapDataStore(context)
    }
}
