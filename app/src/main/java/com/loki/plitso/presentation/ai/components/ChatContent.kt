package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.loki.plitso.R
import com.loki.plitso.presentation.ai.AiViewModel
import com.loki.plitso.presentation.ai.ChatUiState
import com.loki.plitso.presentation.document.MealType
import com.loki.plitso.util.showToast

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    aiViewModel: AiViewModel,
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val uiState by aiViewModel.chatState.collectAsStateWithLifecycle()
    val newMessageId by aiViewModel.newMessageId.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        aiViewModel.onStartNewChat()
    }

    when (uiState) {
        is ChatUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is ChatUiState.Error -> {
            context.showToast((uiState as ChatUiState.Error).message)
        }
        is ChatUiState.Success -> {
            val state = uiState as ChatUiState.Success

            LaunchedEffect(uiState) {
                (uiState as? ChatUiState.Success)?.messages?.size?.let { messagesSize ->
                    if (messagesSize > 0) {
                        lazyListState.animateScrollToItem(messagesSize - 1)
                    }
                }
            }

            if (state.messages.isEmpty()) {
                StartNewChartContent()
            }

            Column(
                modifier =
                    modifier
                        .fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                    state = lazyListState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    items(
                        items = state.messages,
                        key = { it.id },
                    ) { message ->
                        if (message.role == context.getString(R.string.user)) {
                            MessengerItemCard(
                                modifier = Modifier.align(Alignment.End),
                                message = message.content,
                            )
                        } else {
                            ReceiverMessageItemCard(
                                message = message.content,
                                isEffectActive = message.id.toString() == newMessageId,
                                onEffectComplete = {
                                    aiViewModel.resetMessageId()
                                },
                            )
                        }
                    }

                    if (state.isProcessing) {
                        item {
                            ProcessingIndicator()
                        }
                    }
                }
                if (state.error != null) {
                    ErrorMessage(
                        message = state.error,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun StartNewChartContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                MealType.entries.forEach {
                    AsyncImage(
                        model = it.image,
                        contentDescription = it.name,
                    )
                }
            }

            Text(
                text = stringResource(R.string.start_a_chat),
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ProcessingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier.widthIn(max = 340.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                )
                Text(
                    text = stringResource(R.string.thinking),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(16.dp),
        )
    }
}
