package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.R
import com.loki.plitso.presentation.components.TypeWriterTextEffect

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        TypeWriterTextEffect(
            minDelayInMillis = 20,
            maxDelayInMillis = 200,
            text = "Hello, \nuse AI to generate recipe",
            onEffectComplete = { },
        ) {
            Text(
                text = it,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                modifier =
                    Modifier
                        .padding(top = 32.dp)
                        .padding(horizontal = 24.dp)
                        .align(Alignment.TopCenter),
            )
        }

        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ai),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Use your previous meal history to generate suggestion for your next meal",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(.7f),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = navigateToLogin) {
                Text(text = "Login", color = Color.White)
            }
        }
    }
}
