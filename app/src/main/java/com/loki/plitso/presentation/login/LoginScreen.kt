package com.loki.plitso.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.R
import com.loki.plitso.presentation.components.NetworkContainer
import com.loki.plitso.presentation.login.components.GoogleButtonUiContainer
import com.loki.plitso.presentation.login.components.GoogleSignInButton
import com.loki.plitso.util.showToast

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    plitsoViewModel: PlitsoViewModel,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val isNetworkAvailable by plitsoViewModel.isNetworkAvailable.collectAsStateWithLifecycle()
    val loginState by loginViewModel.state.collectAsStateWithLifecycle()
    val googleAuthProvider = loginViewModel.googleAuthProvider

    LaunchedEffect(key1 = loginState.errorMessage) {
        if (loginState.errorMessage.isNotBlank()) {
            context.showToast(loginState.errorMessage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        IconButton(
            onClick = navigateBack,
            modifier =
                Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
        ) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
        }

        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.app_name), fontSize = 28.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(R.string.welcome_back))
            Spacer(modifier = Modifier.height(24.dp))
            GoogleButtonUiContainer(
                modifier = Modifier,
                googleAuthProvider = googleAuthProvider,
                onGoogleSignInResult = { user ->
                    loginViewModel.login(user, navigateBack)
                },
            ) {
                GoogleSignInButton(
                    isEnabled = !loginState.isLoading && isNetworkAvailable,
                    onClick = {
                        this.onClick()
                    },
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (loginState.isLoading) {
                CircularProgressIndicator()
            }
        }

        if (!isNetworkAvailable) {
            NetworkContainer()
        }
    }
}
