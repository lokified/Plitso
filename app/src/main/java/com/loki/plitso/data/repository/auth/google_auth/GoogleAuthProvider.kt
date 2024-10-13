package com.loki.plitso.data.repository.auth.google_auth

import androidx.compose.runtime.Composable

interface GoogleAuthProvider {
    @Composable
    fun getUiProvider(): GoogleAuthUiProvider
}