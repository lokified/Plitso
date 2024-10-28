package com.loki.plitso.presentation.ai

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.R
import com.loki.plitso.presentation.ai.components.ChatContent
import com.loki.plitso.presentation.ai.components.DrawerContent
import com.loki.plitso.presentation.ai.components.FirstOpenContent
import com.loki.plitso.presentation.ai.components.GenerativeContent
import com.loki.plitso.presentation.ai.components.LoginContent
import com.loki.plitso.presentation.document.components.textFieldColors
import com.loki.plitso.util.noIndication
import kotlinx.coroutines.launch

@Composable
fun AIScreen(
    aiViewModel: AiViewModel,
    plitsoViewModel: PlitsoViewModel,
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val user by plitsoViewModel.user.collectAsStateWithLifecycle()
    val uiState by aiViewModel.chatState.collectAsStateWithLifecycle()

    var aiScreenContent by rememberSaveable {
        mutableStateOf(AiScreenContent.FIRST_OPEN)
    }

    val (input, setInput) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (aiScreenContent == AiScreenContent.CHAT || aiScreenContent == AiScreenContent.GENERATIVE) {
        BackHandler {
            aiViewModel.resetChat()
            aiScreenContent = AiScreenContent.FIRST_OPEN
            if (drawerState.isOpen) {
                coroutineScope.launch {
                    drawerState.close()
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = aiScreenContent == AiScreenContent.CHAT,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                DrawerContent(drawerState = drawerState, aiViewModel = aiViewModel)
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (aiScreenContent == AiScreenContent.CHAT) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu icon",
                        )
                    }
                }
                Text(
                    text =
                        if (aiScreenContent == AiScreenContent.CHAT) {
                            (uiState as ChatUiState.Success).title
                        } else {
                            "Ask AI"
                        },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .padding(
                                top =
                                    if (aiScreenContent == AiScreenContent.CHAT) {
                                        16.dp
                                    } else {
                                        0.dp
                                    },
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

            if (!user.isLoggedIn) {
                LoginContent(
                    navigateToLogin = navigateToLogin,
                )
            } else {
                when (aiScreenContent) {
                    AiScreenContent.FIRST_OPEN -> {
                        FirstOpenContent(
                            modifier = Modifier,
                            onPreviewChatScreen = {
                                aiScreenContent = AiScreenContent.CHAT
                            },
                            onPreviewGenScreen = {
                                aiScreenContent = AiScreenContent.GENERATIVE
                            },
                        )
                    }

                    AiScreenContent.CHAT -> {
                        ChatContent(
                            modifier = Modifier.weight(1f),
                            aiViewModel = aiViewModel,
                        )
                    }

                    AiScreenContent.GENERATIVE -> {
                        GenerativeContent(
                            modifier = Modifier.weight(1f),
                            aiViewModel = aiViewModel,
                        )
                    }
                }
            }

            if (aiScreenContent == AiScreenContent.CHAT) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                ) {
                    EditorCard(
                        modifier =
                            Modifier
                                .padding(top = 4.dp)
                                .padding(horizontal = 16.dp)
                                .imePadding(),
                        value = input,
                        onValueChange = { setInput(it) },
                        onClickSend = {
                            if (input.isNotEmpty()) {
                                aiViewModel.askQuestion(input)
                                setInput("")
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorCard(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(.2f),
                    shape = CircleShape,
                ),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { value ->
                onValueChange(value)
            },
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
