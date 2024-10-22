package com.loki.plitso.presentation.ai.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.presentation.ai.AiViewModel
import com.loki.plitso.presentation.components.TypeWriterTextEffect
import com.loki.plitso.presentation.document.MealType

@Composable
fun ParametersContent(
    modifier: Modifier = Modifier,
    aiViewModel: AiViewModel,
    onSuggestClick: () -> Unit,
) {
    val parameters by aiViewModel.parameters.collectAsStateWithLifecycle()
    val aiState by aiViewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            TypeWriterTextEffect(
                text = "Tune your Meal Suggestions",
                maxDelayInMillis = 200,
                onEffectComplete = { /*TODO*/ },
            ) { text ->
                Text(
                    text = text,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            MealTypeSection(
                selectedType = parameters.mealType,
                onChangeSelected = aiViewModel::onMealTypeChange,
            )

            CuisineSection(
                countries = aiViewModel.aiData.countries.distinct(),
                selectedCuisine = parameters.cuisine,
                onChangeSelected = aiViewModel::onCuisineChange,
            )

            MoodSection(
                selectedMood = parameters.mood,
                onChangeSelected = aiViewModel::onMoodChange,
            )

            DietarySection(
                selectedDietary = parameters.dietary,
                onChangeSelected = aiViewModel::onDietaryChange,
            )

            QuickSection(
                selectedQuick = parameters.isQuick,
                onChangeSelected = aiViewModel::isQuickMealChange,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSuggestClick,
                modifier = Modifier.align(Alignment.End),
                enabled = !aiState.isLoading,
            ) {
                if (aiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(text = "Suggest A Meal", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealTypeSection(
    modifier: Modifier = Modifier,
    selectedType: String,
    onChangeSelected: (String) -> Unit,
) {
    Column {
        Text(text = "Select Meal")
        Spacer(modifier = Modifier.height(4.dp))

        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MealType.entries.forEach {
                val selected = it.name == selectedType
                Selectable(
                    label = it.name,
                    selected = selected,
                    onSelected = onChangeSelected,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CuisineSection(
    modifier: Modifier = Modifier,
    countries: List<String>,
    selectedCuisine: String,
    onChangeSelected: (String) -> Unit,
) {
    Column {
        Text(text = "Select Cuisine")
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            countries.forEach {
                val selected = it == selectedCuisine
                Selectable(
                    label = it,
                    selected = selected,
                    onSelected = onChangeSelected,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodSection(
    modifier: Modifier = Modifier,
    selectedMood: String,
    onChangeSelected: (String) -> Unit,
) {
    val moods = listOf("Savory", "Sweet", "Spicy", "Healthy", "Comfort")
    Column {
        Text(text = "Select Mood")
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            moods.forEach {
                val selected = it == selectedMood
                Selectable(
                    label = it,
                    selected = selected,
                    onSelected = onChangeSelected,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietarySection(
    modifier: Modifier = Modifier,
    selectedDietary: String,
    onChangeSelected: (String) -> Unit,
) {
    val dietaries = listOf("Vegeterian", "Vegan", "Glutten-free", "Dairy-free")
    Column {
        Text(text = "Select Dietary (Optional)")
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            dietaries.forEach {
                val selected = it == selectedDietary
                Selectable(
                    label = it,
                    selected = selected,
                    onSelected = onChangeSelected,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickSection(
    modifier: Modifier = Modifier,
    selectedQuick: Boolean,
    onChangeSelected: (Boolean) -> Unit,
) {
    val quicks = listOf("Yes", "No")
    Column {
        Text(text = "Want a Quick Meal?")
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            quicks.forEach { quick ->
                val selected = quick == if (selectedQuick) "Yes" else "No"
                Selectable(
                    label = quick,
                    selected = selected,
                    onSelected = {
                        onChangeSelected(
                            it == "Yes",
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun Selectable(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onSelected: (text: String) -> Unit,
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = { onSelected(label) },
        label = {
            Text(text = label)
        },
    )
}
