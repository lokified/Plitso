package com.loki.plitso.data.repository.auth

import com.loki.plitso.data.remote.mealdb.models.User
import com.loki.plitso.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>
    fun authenticate(token: String): Flow<Resource<User>>
    fun logOut(): Flow<Resource<String>>
}