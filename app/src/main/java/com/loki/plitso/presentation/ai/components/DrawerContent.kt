package com.loki.plitso.presentation.ai.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.R
import com.loki.plitso.data.local.models.ChatHistory
import com.loki.plitso.presentation.ai.AiViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    aiViewModel: AiViewModel,
) {
    val chatHistory by aiViewModel.chatHistory.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        NewChatButton(
            onClick = {
                aiViewModel.onStartNewChat()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        if (chatHistory.isEmpty()) {
            EmptyChatHistory()
        } else {
            ChatHistoryList(
                chats = chatHistory,
                onChatSelected = { chat ->
                    aiViewModel.setCurrentChat(chat.id)
                    coroutineScope.launch { drawerState.close() }
                },
                onDeleteChat = { aiViewModel.deleteChat(it.id) },
            )
        }
    }
}

@Composable
private fun NewChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.start_new_chat),
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun EmptyChatHistory(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.no_chat_history),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun ChatHistoryList(
    chats: List<ChatHistory>,
    onChatSelected: (ChatHistory) -> Unit,
    onDeleteChat: (ChatHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            items = chats.reversed(),
            key = { it.id },
        ) { chat ->
            ChatHistoryItem(
                chat = chat,
                onChatSelected = onChatSelected,
                onDeleteChat = onDeleteChat,
            )
        }
    }
}

@Composable
private fun ChatHistoryItem(
    chat: ChatHistory,
    onChatSelected: (ChatHistory) -> Unit,
    onDeleteChat: (ChatHistory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier =
                Modifier
                    .clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(R.string.select_chat, chat.title),
                    ) { onChatSelected(chat) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text =
                        remember(chat.startedOn) {
                            DateUtils.getRelativeTimeSpanString(
                                chat.startedOn.time,
                                System.currentTimeMillis(),
                                DateUtils.MINUTE_IN_MILLIS,
                            ).toString()
                        },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }

            IconButton(
                onClick = { onDeleteChat(chat) },
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
