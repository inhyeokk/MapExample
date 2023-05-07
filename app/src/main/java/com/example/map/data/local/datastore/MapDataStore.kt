package com.example.map.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.map.extension.getValueFlow
import com.example.map.extension.set
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
    private val dataStore = context.dataStore

    fun getTrackingMode(): Flow<CurrentLocationTrackingMode> {
        return dataStore.getValueFlow(
            stringPreferencesKey(TRACKING_MODE), CurrentLocationTrackingMode.TrackingModeOff.name
        ).map {
            CurrentLocationTrackingMode.valueOf(it)
        }
    }

    suspend fun setTrackingMode(trackingMode: CurrentLocationTrackingMode) {
        dataStore.set(stringPreferencesKey(TRACKING_MODE), trackingMode.name)
    }

    companion object {
        private const val PREF_NAME = "data_store"
        private const val TRACKING_MODE = "TRACKING_MODE"
    }
}
