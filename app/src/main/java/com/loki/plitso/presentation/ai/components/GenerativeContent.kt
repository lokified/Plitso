package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loki.plitso.presentation.ai.AiState
import com.loki.plitso.presentation.ai.AiViewModel

@Composable
fun GenerativeContent(
    modifier: Modifier = Modifier,
    aiState: AiState,
    aiViewModel: AiViewModel,
) {
    var showSuggestion by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        if (showSuggestion) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                ReceiverMessageItemCard(
                    modifier = Modifier.padding(16.dp),
                    message = aiState.generativeAnswer,
                    isEffectActive = true,
                )
            }
        } else {
            ParametersContent(
                aiViewModel = aiViewModel,
                onSuggestClick = {
                    aiViewModel.generateSuggestions {
                        showSuggestion = true
                    }
                },
            )
        }
    }
}
