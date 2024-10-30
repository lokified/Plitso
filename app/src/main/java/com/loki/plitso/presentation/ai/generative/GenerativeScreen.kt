package com.loki.plitso.presentation.ai.generative

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.presentation.ai.components.AiTopBar
import com.loki.plitso.presentation.ai.components.ParametersContent
import com.loki.plitso.presentation.ai.components.ReceiverMessageItemCard
import com.loki.plitso.util.showToast

@Composable
fun GenerativeScreen(
    generativeViewModel: GenerativeViewModel,
    navigateBack: () -> Unit,
) {
    val uiState by generativeViewModel.genState.collectAsStateWithLifecycle()

    var showSuggestion by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotEmpty()) {
            context.showToast(uiState.errorMessage)
        }
    }

    if (showSuggestion) {
        BackHandler {
            showSuggestion = false
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        AiTopBar(navigateBack = navigateBack)
        if (showSuggestion) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                ReceiverMessageItemCard(
                    modifier = Modifier.padding(16.dp),
                    message = uiState.generativeAnswer,
                    isEffectActive = true,
                )
            }
        } else {
            ParametersContent(
                generativeViewModel = generativeViewModel,
                onSuggestClick = {
                    generativeViewModel.generateSuggestions {
                        showSuggestion = true
                    }
                },
            )
        }
    }
}
