package com.loki.plitso.data.repository.auth.googleAuth

import androidx.compose.runtime.Composable

interface GoogleAuthProvider {
    @Composable
    fun getUiProvider(): GoogleAuthUiProvider
}
