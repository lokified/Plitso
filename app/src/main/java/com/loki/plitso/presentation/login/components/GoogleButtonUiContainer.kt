package com.loki.plitso.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.R
import com.loki.plitso.data.remote.mealdb.models.User
import com.loki.plitso.data.repository.auth.googleAuth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun GoogleButtonUiContainer(
    modifier: Modifier = Modifier,
    googleAuthProvider: GoogleAuthProvider,
    onGoogleSignInResult: (User?) -> Unit,
    content: @Composable GoogleButtonUiContainerScope.() -> Unit,
) {
    val googleAuthUiProvider = googleAuthProvider.getUiProvider()
    val coroutineScope = rememberCoroutineScope()

    val uiContainerScope =
        remember {
            object : GoogleButtonUiContainerScope {
                override fun onClick() {
                    coroutineScope.launch {
                        val googleUser = googleAuthUiProvider.signIn()
                        onGoogleSignInResult(googleUser)
                    }
                }
            }
        }

    Box(
        modifier = modifier,
        content = { uiContainerScope.content() },
    )
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp),
        shape = CircleShape,
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 8.dp,
            ),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
        enabled = isEnabled,
    ) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "google_icon",
            modifier = Modifier.size(25.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(R.string.sign_in_with_google), fontSize = 18.sp)
    }
}

interface GoogleButtonUiContainerScope {
    fun onClick()
}
