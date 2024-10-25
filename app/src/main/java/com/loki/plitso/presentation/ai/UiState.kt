package com.loki.plitso.presentation.ai

import com.loki.plitso.data.local.models.AiAnswer

sealed interface ChatUiState {
    data object Loading : ChatUiState

    data class Success(
        val messages: List<AiAnswer> = emptyList(),
        val title: String = "",
        val isProcessing: Boolean = false,
        val error: String? = null,
    ) : ChatUiState

    data class Error(val message: String) : ChatUiState
}

data class GenerativeState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val generativeAnswer: String = "",
)

data class GenerativeParameters(
    val mealType: String = "",
    val cuisine: String = "",
    val mood: String = "",
    val dietary: String = "",
    val isQuick: Boolean = false,
)
