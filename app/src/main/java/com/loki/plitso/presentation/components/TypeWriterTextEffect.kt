package com.loki.plitso.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TypeWriterTextEffect(
    text: String,
    minDelayInMillis: Long = 10,
    maxDelayInMillis: Long = 50,
    minCharacterChunk: Int = 1,
    maxCharacterChunk: Int = 5,
    onEffectComplete: () -> Unit,
    displayTextContent: @Composable (displayedText: String) -> Unit
) {
    require(minDelayInMillis <= maxDelayInMillis) {
        "TypeWriterTextEffect: Invalid delay range. minDelayInMillis ($minDelayInMillis) must be less than or equal to maxDelayInMillis ($maxDelayInMillis)"
    }

    require(minCharacterChunk <= maxCharacterChunk) {
        "TypeWriterTextEffect: Invalid chunk range. minCharacterChunk ($minCharacterChunk) must be less than or equal to maxCharacterChunk ($maxCharacterChunk)"
    }

    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        val textLength = text.length
        var endIndex = 0

        while (endIndex < textLength) {
            endIndex = minOf(
                endIndex + Random.nextInt(minCharacterChunk, maxCharacterChunk + 1),
                textLength
            )
            displayedText = text.substring(startIndex = 0, endIndex = endIndex)
            delay(Random.nextLong(minDelayInMillis, maxDelayInMillis))
        }

        onEffectComplete()
    }
    displayTextContent(displayedText)
}