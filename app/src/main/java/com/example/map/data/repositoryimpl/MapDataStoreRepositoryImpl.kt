package com.example.map.data.repositoryimpl

import com.example.map.data.local.datastore.MapDataStore
import com.example.map.domain.repository.MapDataStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import javax.inject.Inject

@ViewModelScoped
class MapDataStoreRepositoryImpl @Inject constructor(
    private val mapDataStore: MapDataStore
) : MapDataStoreRepository {
    override fun getTrackingMode(): Flow<CurrentLocationTrackingMode> {
        return mapDataStore.getTrackingMode()
    }

    override suspend fun setTrackingMode(trackingMode: CurrentLocationTrackingMode) {
        mapDataStore.setTrackingMode(trackingMode)
    }
}
