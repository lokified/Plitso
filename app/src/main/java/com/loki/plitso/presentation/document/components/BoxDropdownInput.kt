package com.loki.plitso.presentation.document.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.loki.plitso.presentation.document.MealType
import com.loki.plitso.util.noIndication

@Composable
fun BoxDropdownInput(
    modifier: Modifier = Modifier,
    selectedOption: String,
    onChangeOption: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = MealType.entries.find { it.name == selectedOption } ?: MealType.BREAKFAST

    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        BoxInput(
            value = selectedOption,
            onValueChange = onChangeOption,
            icon = icon.image,
            placeholder = "Select Meal Time",
            modifier = Modifier.noIndication { expanded = !expanded },
        )

        AnimatedVisibility(
            visible = expanded,
            modifier =
                Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp,
                        ),
                    ).background(MaterialTheme.colorScheme.surface),
        ) {
            Column {
                MealType.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            onChangeOption(option.name)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
