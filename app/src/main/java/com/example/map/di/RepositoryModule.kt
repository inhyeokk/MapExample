package com.example.map.di

import com.example.map.data.repositoryimpl.FavoriteDocumentRepositoryImpl
import com.example.map.data.repositoryimpl.LocalSearchRepositoryImpl
import com.example.map.data.repositoryimpl.MapDataStoreRepositoryImpl
import com.example.map.domain.repository.FavoriteDocumentRepository
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.repository.MapDataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindFavoriteDocumentRepository(repository: FavoriteDocumentRepositoryImpl): FavoriteDocumentRepository

    @ViewModelScoped
    @Binds
    abstract fun bindLocalSearchRepository(repository: LocalSearchRepositoryImpl): LocalSearchRepository

    @ViewModelScoped
    @Binds
    abstract fun bindMapDataStoreRepository(repository: MapDataStoreRepositoryImpl): MapDataStoreRepository

}
