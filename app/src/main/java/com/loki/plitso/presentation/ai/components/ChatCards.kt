package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.loki.plitso.R
import com.loki.plitso.presentation.components.TypeWriterTextEffect
import com.mikepenz.markdown.m3.Markdown

@Composable
fun MessengerItemCard(
    modifier: Modifier = Modifier,
    message: String = "",
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp, bottomStart = 25.dp),
    ) {
        Text(
            modifier =
            Modifier
                .wrapContentSize()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            text = message,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
        )
    }
}

@Composable
fun ReceiverMessageItemCard(
    modifier: Modifier = Modifier,
    message: String = "",
    isEffectActive: Boolean = false,
    onEffectComplete: () -> Unit = {},
) {
    var isEffectComplete by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
    ) {
        Surface(
            modifier =
            Modifier
                .wrapContentSize()
                .align(Alignment.Bottom),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp,
        ) {
            Image(
                modifier =
                Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .size(18.dp),
                painter = painterResource(id = R.drawable.ai),
                contentDescription = "",
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomEnd = 25.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {

            if (isEffectActive && !isEffectComplete) {
                TypeWriterTextEffect(
                    text = message,
                    onEffectComplete = {
                        onEffectComplete()
                        isEffectComplete = true
                    },
                ) { text ->
                    Markdown(
                        content = text,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                Markdown(
                    content = message,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
