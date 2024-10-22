package com.loki.plitso.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)
