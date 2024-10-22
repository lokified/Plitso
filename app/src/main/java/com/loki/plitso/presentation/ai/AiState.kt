package com.loki.plitso.presentation.ai

import com.loki.plitso.data.local.models.AiAnswer

data class AiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val messages: List<AiAnswer> = emptyList(),
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val generativeAnswer: String = "",
)

data class GenerativeParameters(
    val mealType: String = "",
    val cuisine: String = "",
    val mood: String = "",
    val dietary: String = "",
    val isQuick: Boolean = false,
)
