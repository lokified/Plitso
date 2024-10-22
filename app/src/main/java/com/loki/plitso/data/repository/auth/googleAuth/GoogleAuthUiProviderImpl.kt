package com.loki.plitso.data.repository.auth.googleAuth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.loki.plitso.data.remote.mealdb.models.User
import timber.log.Timber

class GoogleAuthUiProviderImpl(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val credentials: GoogleAuthCredentials,
) : GoogleAuthUiProvider {
    override suspend fun signIn(): User? =
        try {
            val credential =
                credentialManager
                    .getCredential(
                        context = context,
                        request = getCredentialRequest(),
                    ).credential

            getGoogleUserFromCredential(credential)
        } catch (e: GetCredentialException) {
            Timber.e(e.message)
            null
        } catch (e: NullPointerException) {
            Timber.d(e.message)
            null
        }

    private fun getGoogleUserFromCredential(credential: Credential): User? =
        when {
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    User(
                        username = googleIdTokenCredential.displayName,
                        imageUrl = googleIdTokenCredential.profilePictureUri,
                        idToken = googleIdTokenCredential.idToken,
                    )
                } catch (e: GoogleIdTokenParsingException) {
                    Timber.tag("google id token err").d(e)
                    null
                }
            }

            else -> null
        }

    private fun getCredentialRequest(): GetCredentialRequest =
        GetCredentialRequest
            .Builder()
            .addCredentialOption(getGoogleIdOptions(credentials.serverId))
            .build()

    private fun getGoogleIdOptions(serverClientId: String): GetGoogleIdOption =
        GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(serverClientId)
            .build()
}
