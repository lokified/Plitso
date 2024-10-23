package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loki.plitso.presentation.ai.AiState

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    aiState: AiState,
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(aiState.messages.size) {
        scrollToEnd(lazyListState, aiState.messages.size)
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
            items(aiState.messages) { message ->
                if (message.role == "user") {
                    MessengerItemCard(
                        modifier = Modifier.align(Alignment.End),
                        message = message.content,
                    )
                } else {
                    var isStartTypeEffect by remember { mutableStateOf(false) }

                    LaunchedEffect(aiState.isLoading) {
                        if (aiState.isLoading && message == aiState.messages.last()) {
                            isStartTypeEffect = true
                        }
                    }

                    if (message == aiState.messages.last() && isStartTypeEffect) {
                        ReceiverMessageItemCard(
                            message = message.content,
                            isEffectActive = true,
                            onEffectComplete = {
                                isStartTypeEffect = false
                            },
                        )
                    } else {
                        ReceiverMessageItemCard(
                            message = message.content,
                        )
                    }
                }
            }

            if (aiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

suspend fun scrollToEnd(
    listState: LazyListState,
    itemCount: Int,
) {
    val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()

    if (itemCount > 0) {
        if (lastVisibleItem != null && lastVisibleItem.index == itemCount - 1) {
            val viewportHeight = listState.layoutInfo.viewportEndOffset
            val lastItemBottom = lastVisibleItem.offset + lastVisibleItem.size
            if (lastItemBottom > viewportHeight) {
                listState.scrollToItem(itemCount - 1, lastItemBottom - viewportHeight)
            }
        } else {
            listState.scrollToItem(itemCount - 1)
        }
    }
}
