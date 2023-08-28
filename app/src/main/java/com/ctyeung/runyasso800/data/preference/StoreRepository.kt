package com.ctyeung.runyasso800.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/*
 * https://developer.android.com/topic/libraries/architecture/datastore
 */
class StoreRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val STORE_NAME = "aeris_data_store"
    private val PREFS_NAME = "aeris_preference"
    private val prefStore: DataStore<Preferences>

    companion object {
        private const val CONFIG = "config"
        val CONFIG_KEY: Preferences.Key<String> = preferencesKey<String>(CONFIG)

        private const val GOAL = "goal"
        val GOAL_KEY: Preferences.Key<String> = preferencesKey<String>(GOAL)
    }

    var configFlow: Flow<String>
    var goalFlow: Flow<String>

    init {
        prefStore = context.createDataStore(
            name = STORE_NAME,
            migrations = listOf(SharedPreferencesMigration(context, PREFS_NAME))
        )

        configFlow = prefStore.data.map { preferences ->
            preferences[CONFIG_KEY] ?: ""
        }

        goalFlow = prefStore.data.map { preferences ->
            preferences[GOAL_KEY] ?: ""
        }
    }

    fun getBoolean(flag: String): Flow<Boolean?> {
        return prefStore.data.map { it[preferencesKey(flag)] }
    }

    suspend fun setBoolean(flag: String, isTrue: Boolean) {
        prefStore.edit {
            it[preferencesKey<Boolean>(flag)] = isTrue
        }
    }

    fun getString(key: Preferences.Key<String>): Flow<String> {
        return prefStore.data.map {
            it[key] ?: ""
        }
    }

    suspend fun setString(key: Preferences.Key<String>, str: String) {
        prefStore.edit {
            it[key] = str
        }
    }

    fun getLong(flag: String): Flow<Long> {
        return prefStore.data.map { it[preferencesKey(flag)] ?: -1L }
    }

    suspend fun setLong(flag: String, num: Long) {
        prefStore.edit {
            it[preferencesKey<Long>(flag)] = num
        }
    }

    fun getInt(flag: String): Flow<Int?> {
        return prefStore.data.map { it[preferencesKey(flag)] }
    }

    suspend fun setInt(flag: String, num: Int) {
        prefStore.edit {
            it[preferencesKey<Int>(flag)] = num
        }
    }
}