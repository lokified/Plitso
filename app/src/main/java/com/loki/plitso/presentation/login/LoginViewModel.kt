package com.loki.plitso.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.datastore.DatastoreStorage
import com.loki.plitso.data.local.datastore.LocalUser
import com.loki.plitso.data.remote.mealdb.models.User
import com.loki.plitso.data.repository.auth.AuthRepository
import com.loki.plitso.data.repository.auth.googleAuth.GoogleAuthProvider
import com.loki.plitso.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val datastoreStorage: DatastoreStorage,
    val googleAuthProvider: GoogleAuthProvider,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun login(
        user: User?,
        onSuccess: () -> Unit,
    ) {
        user?.let {
            viewModelScope.launch {
                authRepository.authenticate(it.idToken).collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.value =
                                _state.value.copy(
                                    isLoading = true,
                                )
                        }

                        is Resource.Success -> {
                            val loggedUser = result.data
                            datastoreStorage.saveLocalUser(
                                LocalUser(
                                    userId = loggedUser.id,
                                    email = loggedUser.email,
                                    username = loggedUser.username ?: "",
                                    imageUrl = loggedUser.imageUrl.toString(),
                                    isLoggedIn = true,
                                ),
                            )

                            _state.value =
                                _state.value.copy(
                                    isLoading = false,
                                )

                            onSuccess()
                        }

                        is Resource.Error -> {
                            _state.value =
                                _state.value.copy(
                                    isLoading = false,
                                    errorMessage = result.message,
                                )
                            Log.d("login err", result.message)
                        }
                    }
                }
            }
        }
    }
}
