package com.example.map.di

import com.example.map.data.remote.api.KakaoLocalServiceFactory
import com.example.map.data.remote.api.LocalSearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLocalSearchApi(): LocalSearchApi = KakaoLocalServiceFactory.create(LocalSearchApi::class.java)
}
