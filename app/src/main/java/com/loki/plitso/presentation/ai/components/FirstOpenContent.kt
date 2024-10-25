package com.loki.plitso.presentation.ai.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loki.plitso.R

@Composable
fun FirstOpenContent(
    modifier: Modifier = Modifier,
    onPreviewChatScreen: () -> Unit,
    onPreviewGenScreen: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.what_would_you),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        BoxSelect(
            icon = Icons.Default.Restaurant,
            title = stringResource(R.string.generate_new_meal),
            description = stringResource(R.string.personalized_meal),
            accentColor = MaterialTheme.colorScheme.primary,
            onClick = onPreviewGenScreen,
            modifier = Modifier.weight(1f),
        )

        BoxSelect(
            icon = Icons.AutoMirrored.Filled.Chat,
            title = "Chat with AI",
            description = stringResource(R.string.get_answers),
            accentColor = MaterialTheme.colorScheme.secondary,
            onClick = onPreviewChatScreen,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun BoxSelect(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    val isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        label = "scale anim",
    )

    Surface(
        modifier =
            modifier
                .scale(scale)
                .fillMaxWidth()
                .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = if (isPressed) 0.dp else 4.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Accent circle background
            Box(
                modifier =
                    Modifier
                        .size(100.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 40.dp, y = (-40).dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.1f)),
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(32.dp),
                )

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp,
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onClick,
                ) {
                    Text(
                        text = stringResource(R.string.get_started),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentColor,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
