package com.loki.plitso.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.loki.plitso.data.local.datastore.DatastoreStorage
import com.loki.plitso.data.local.datastore.DatastoreStorageImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

private const val USER_PREFERENCE = "user_preference"

val datastoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                get<Context>().preferencesDataStoreFile(USER_PREFERENCE)
            }
        )
    }
    single<DatastoreStorage> { DatastoreStorageImpl(get()) }
}