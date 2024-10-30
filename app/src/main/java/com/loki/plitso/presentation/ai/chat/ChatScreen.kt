package com.loki.plitso.presentation.ai.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.R
import com.loki.plitso.presentation.ai.ChatUiState
import com.loki.plitso.presentation.ai.components.DrawerContent
import com.loki.plitso.presentation.ai.components.ErrorMessage
import com.loki.plitso.presentation.ai.components.MessengerItemCard
import com.loki.plitso.presentation.ai.components.ProcessingIndicator
import com.loki.plitso.presentation.ai.components.ReceiverMessageItemCard
import com.loki.plitso.presentation.ai.components.StartNewChartContent
import com.loki.plitso.presentation.components.NetworkContainer
import com.loki.plitso.presentation.document.components.textFieldColors
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.showToast
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    plitsoViewModel: PlitsoViewModel,
    chatViewModel: ChatViewModel,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val isNetworkAvailable by plitsoViewModel.isNetworkAvailable.collectAsStateWithLifecycle()
    val uiState by chatViewModel.chatState.collectAsStateWithLifecycle()
    val newMessageId by chatViewModel.newMessageId.collectAsStateWithLifecycle()

    val (input, setInput) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(key1 = Unit) {
        chatViewModel.onStartNewChat()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                DrawerContent(drawerState = drawerState, chatViewModel = chatViewModel)
            }
        },
    ) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    uiState = uiState,
                    onToggleDrawer = {
                        coroutineScope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    },
                    navigateBack = navigateBack,
                )
            },
            bottomBar = {
                Column {
                    if (!isNetworkAvailable) {
                        NetworkContainer()
                    }

                    ChatInput(
                        modifier =
                            Modifier
                                .padding(top = 4.dp)
                                .padding(horizontal = 16.dp)
                                .imePadding(),
                        value = input,
                        isEnabled = isNetworkAvailable,
                        onValueChange = { setInput(it) },
                        onClickSend = {
                            if (input.isNotEmpty()) {
                                chatViewModel.askQuestion(input)
                                setInput("")
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        },
                    )
                }
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (uiState) {
                    is ChatUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    is ChatUiState.Error -> {
                        context.showToast((uiState as ChatUiState.Error).message)
                    }

                    is ChatUiState.Success -> {
                        ChatList(
                            uiState = uiState,
                            newMessageId = newMessageId,
                            onEffectComplete = chatViewModel::resetMessageId,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    onToggleDrawer: () -> Unit,
    navigateBack: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onToggleDrawer,
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "menu icon",
            )
        }

        Text(
            text = (uiState as ChatUiState.Success).title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(
                        top = 16.dp,
                    ),
        )

        IconButton(
            onClick = navigateBack,
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun ChatList(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    newMessageId: String?,
    onEffectComplete: () -> Unit,
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    val state = uiState as ChatUiState.Success

    LaunchedEffect(uiState) {
        (uiState as? ChatUiState.Success)?.messages?.size?.let { messagesSize ->
            if (messagesSize > 0) {
                lazyListState.animateScrollToItem(messagesSize - 1)
            }
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        if (state.messages.isEmpty()) {
            StartNewChartContent()
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth(),
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
                        modifier = Modifier,
                        message = message.content,
                    )
                } else {
                    ReceiverMessageItemCard(
                        message = message.content,
                        isEffectActive = message.id.toString() == newMessageId,
                        onEffectComplete = onEffectComplete,
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

@Composable
private fun ChatInput(
    modifier: Modifier = Modifier,
    value: String,
    isEnabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
    ) {
        TextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(.2f),
                        shape = CircleShape,
                    ),
            value = value,
            enabled = isEnabled,
            onValueChange = { value ->
                onValueChange(value)
            },
            shape = CircleShape,
            placeholder = {
                Text(
                    text = stringResource(R.string.write_your_message),
                    color = MaterialTheme.colorScheme.onBackground.copy(.2f),
                )
            },
            trailingIcon = {
                Icon(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .noIndication {
                                onClickSend()
                            },
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "send icon",
                )
            },
            colors = textFieldColors(),
        )
    }
}
