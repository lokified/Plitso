package com.loki.plitso.data.remote.mealdb.models

import android.net.Uri

data class User(
    val idToken: String = "",
    val id: String = "",
    val email: String = "",
    val username: String? = null,
    val imageUrl: Uri? = null,
    val password: String = "",
)
