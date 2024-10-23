package com.loki.plitso.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.IMAGE_URL_KEY
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.THEME_KEY
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.USER_EMAIL_KEY
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.USER_ID_KEY
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.USER_LOGGEDIN_KEY
import com.loki.plitso.data.local.datastore.DatastoreStorage.UserPreferences.USER_NAME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastoreStorageImpl(
    private val datastore: DataStore<Preferences>,
) : DatastoreStorage {
    override suspend fun saveLocalUser(localUser: LocalUser) {
        datastore.edit { preference ->
            preference[USER_ID_KEY] = localUser.userId
            preference[USER_EMAIL_KEY] = localUser.email
            preference[USER_NAME_KEY] = localUser.username
            preference[IMAGE_URL_KEY] = localUser.imageUrl ?: ""
            preference[USER_LOGGEDIN_KEY] = localUser.isLoggedIn
        }
    }

    override fun getLocalUser(): Flow<LocalUser> =
        datastore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY] ?: ""
            val email = preferences[USER_EMAIL_KEY] ?: ""
            val username = preferences[USER_NAME_KEY] ?: ""
            val imageUrl = preferences[IMAGE_URL_KEY] ?: ""
            val isLoggedIn = preferences[USER_LOGGEDIN_KEY] ?: false

            LocalUser(userId, email, username, imageUrl, isLoggedIn)
        }

    override suspend fun saveAppTheme(theme: Theme) {
        datastore.edit { preference ->
            preference[THEME_KEY] = theme.name
        }
    }

    override fun getAppTheme(): Flow<String> =
        datastore.data.map { preferences ->
            preferences[THEME_KEY] ?: Theme.System.name
        }
}
