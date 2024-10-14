package com.loki.plitso.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.datastore.DatastoreStorage
import com.loki.plitso.data.local.datastore.LocalUser
import com.loki.plitso.data.local.datastore.Theme
import com.loki.plitso.data.repository.auth.AuthRepository
import com.loki.plitso.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val datastoreStorage: DatastoreStorage,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    init {
        getTheme()
    }

    private fun getTheme() {
        viewModelScope.launch {
            datastoreStorage.getAppTheme().collect { theme ->
                _state.value = _state.value.copy(
                    theme = theme
                )
            }
        }
    }

    fun onChangeTheme(theme: String) {
        viewModelScope.launch {
            val selectedTheme = when (theme) {
                Theme.Dark.name -> Theme.Dark
                Theme.Light.name -> Theme.Light
                else -> Theme.System
            }
            datastoreStorage.saveAppTheme(selectedTheme)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = AccountState(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = AccountState(
                            isLoading = false
                        )
                        datastoreStorage.saveLocalUser(
                            LocalUser()
                        )
                    }

                    is Resource.Error -> {
                        _state.value = AccountState(
                            message = result.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}