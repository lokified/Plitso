package com.loki.plitso.data.repository.auth.googleAuth

import com.loki.plitso.data.remote.mealdb.models.User

interface GoogleAuthUiProvider {
    suspend fun signIn(): User?
}
