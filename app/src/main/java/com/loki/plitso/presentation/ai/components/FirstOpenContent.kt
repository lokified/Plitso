package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.util.noIndication


@Composable
fun FirstOpenContent(
    modifier: Modifier = Modifier,
    onPreviewChatScreen: () -> Unit,
    onPreviewGenScreen: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        BoxSelect(
            modifier = Modifier.weight(.5f),
            text = "Generate Meal based on the previous meal",
            onClick = onPreviewGenScreen
        )

        BoxSelect(
            modifier = Modifier.weight(.5f),
            text = "Ask AI anything about meals, nutrition etc",
            onClick = onPreviewChatScreen
        )
    }
}

@Composable
fun BoxSelect(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            )
            .noIndication { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(.7f),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Output,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(.7f)
            )
        }
    }
}