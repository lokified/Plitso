package com.loki.plitso.presentation.account

data class AccountState(
    val isLoading: Boolean = false,
    val message: String = "",
    val language: String = "",
    val theme: String = ""
)
