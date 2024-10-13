package com.loki.plitso.presentation.account

data class AccountState(
    val email: String = "",
    val username: String = "",
    val imageUrl: String? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val message: String = "",
    val language: String = "",
    val theme: String = ""
)
