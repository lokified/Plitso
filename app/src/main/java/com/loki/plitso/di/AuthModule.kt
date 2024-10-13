package com.loki.plitso.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loki.plitso.BuildConfig
import com.loki.plitso.data.repository.auth.AuthRepository
import com.loki.plitso.data.repository.auth.AuthRepositoryImpl
import com.loki.plitso.data.repository.auth.google_auth.GoogleAuthCredentials
import com.loki.plitso.data.repository.auth.google_auth.GoogleAuthProvider
import com.loki.plitso.data.repository.auth.google_auth.GoogleAuthProviderImpl
import com.loki.plitso.data.repository.auth.google_auth.GoogleAuthUiProvider
import com.loki.plitso.data.repository.auth.google_auth.GoogleAuthUiProviderImpl
import com.loki.plitso.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<CredentialManager> { CredentialManager.create(get()) }
    single<GoogleAuthProvider> {
        GoogleAuthProviderImpl(
            GoogleAuthCredentials(
                serverId = BuildConfig.serverID
            ),
            get()
        )
    }
    single<GoogleAuthUiProvider> { GoogleAuthUiProviderImpl(get(), get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}