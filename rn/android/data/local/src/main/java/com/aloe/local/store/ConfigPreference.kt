package com.aloe.local.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

object ConfigPreference {
  private lateinit var dataStore: DataStore<Preferences>
  private fun getDataStore(context: Context): DataStore<Preferences> {
    if (!this::dataStore.isInitialized) {
      dataStore = PreferenceDataStoreFactory.create {
        File("${context.filesDir.path}/datastore", "config.preferences_pb")
      }
    }
    return dataStore
  }

  suspend fun saveString(context: Context, key: String, value: String) {
    getDataStore(context).updateData {
      it.toMutablePreferences().apply { set(stringPreferencesKey(key), value) }
    }
  }

  fun getString(context: Context, key: String): Flow<String?> =
    getDataStore(context).data.map { it[stringPreferencesKey(key)] }
}