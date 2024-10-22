package com.loki.plitso.data.repository.auth.googleAuth

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager

class GoogleAuthProviderImpl(
    private val credentials: GoogleAuthCredentials,
    private val credentialManager: CredentialManager,
) : GoogleAuthProvider {
    @Composable
    override fun getUiProvider(): GoogleAuthUiProvider {
        val context = LocalContext.current
        return GoogleAuthUiProviderImpl(
            context = context,
            credentialManager = credentialManager,
            credentials = credentials,
        )
    }
}
