package com.example.map.domain.repository

import kotlinx.coroutines.flow.Flow
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode

interface MapDataStoreRepository {
    fun getTrackingMode(): Flow<CurrentLocationTrackingMode>
    suspend fun setTrackingMode(trackingMode: CurrentLocationTrackingMode)
}
