package com.loki.plitso.presentation.ai

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.presentation.ai.components.AiTopBar
import com.loki.plitso.presentation.ai.components.FirstOpenContent
import com.loki.plitso.presentation.ai.components.LoginContent

@Composable
fun AIScreen(
    plitsoViewModel: PlitsoViewModel,
    navigateToLogin: () -> Unit,
    navigateToChatScreen: () -> Unit,
    navigateToGenerativeScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    val user by plitsoViewModel.user.collectAsStateWithLifecycle()

    Column {
        AiTopBar(navigateBack = navigateBack)

        if (!user.isLoggedIn) {
            LoginContent(
                navigateToLogin = navigateToLogin,
            )
        } else {
            FirstOpenContent(
                modifier = Modifier,
                onNavigateToChatScreen = navigateToChatScreen,
                onNavigateToGenScreen = navigateToGenerativeScreen,
            )
        }
    }
}
