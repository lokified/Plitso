package com.loki.plitso.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface DatastoreStorage {
    suspend fun saveLocalUser(localUser: LocalUser)

    fun getLocalUser(): Flow<LocalUser>

    suspend fun saveAppTheme(theme: Theme)

    fun getAppTheme(): Flow<String>

    object UserPreferences {
        val USER_ID_KEY = stringPreferencesKey("user_id_key")
        val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        val IMAGE_URL_KEY = stringPreferencesKey("image_url_key")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")
        val USER_LOGGEDIN_KEY = booleanPreferencesKey("user_loggedin_key")
        val THEME_KEY = stringPreferencesKey("theme_key")
    }
}
