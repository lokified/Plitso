package com.loki.plitso.presentation.ai

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.presentation.ai.components.ChatContent
import com.loki.plitso.presentation.ai.components.FirstOpenContent
import com.loki.plitso.presentation.ai.components.GenerativeContent
import com.loki.plitso.presentation.ai.components.LoginContent
import com.loki.plitso.presentation.document.components.textFieldColors
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.showToast

@Composable
fun AIScreen(
    aiViewModel: AiViewModel,
    plitsoViewModel: PlitsoViewModel,
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit,
) {
    val user by plitsoViewModel.user.collectAsStateWithLifecycle()
    val aiState by aiViewModel.state.collectAsStateWithLifecycle()

    var aiScreenContent by rememberSaveable {
        mutableStateOf(AiScreenContent.FIRST_OPEN)
    }

    val (input, setInput) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (aiScreenContent == AiScreenContent.CHAT || aiScreenContent == AiScreenContent.GENERATIVE) {
        BackHandler {
            aiScreenContent = AiScreenContent.FIRST_OPEN
        }
    }

    val context = LocalContext.current

    LaunchedEffect(aiState.errorMessage) {
        if (aiState.errorMessage.isNotEmpty()) {
            context.showToast(aiState.errorMessage)
        }
    }

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
            Text(
                text = "Ask AI",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = navigateBack,
                modifier =
                    Modifier
                        .padding(8.dp),
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
                        aiState = aiState,
                    )
                }

                AiScreenContent.GENERATIVE -> {
                    GenerativeContent(
                        modifier = Modifier.weight(1f),
                        aiState = aiState,
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
                        .background(MaterialTheme.colorScheme.background)
                        .imePadding(),
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
                            keyboardController?.hide()
                        }
                    },
                )
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
                    text = "Write your message",
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
