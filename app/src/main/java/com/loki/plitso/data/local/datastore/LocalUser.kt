package com.loki.plitso.data.local.datastore

data class LocalUser(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val imageUrl: String? = null,
    val isLoggedIn: Boolean = false
)