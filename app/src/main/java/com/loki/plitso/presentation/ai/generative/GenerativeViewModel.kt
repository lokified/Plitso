package com.loki.plitso.presentation.ai.generative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.loki.plitso.presentation.ai.AiData
import com.loki.plitso.presentation.ai.GenerativeParameters
import com.loki.plitso.presentation.ai.GenerativeState
import com.loki.plitso.presentation.ai.PromptUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenerativeViewModel(
    private val generativeModel: GenerativeModel,
    val aiData: AiData,
) : ViewModel() {
    private val _genState = MutableStateFlow(GenerativeState())
    val genState = _genState.asStateFlow()

    private val _parameters = MutableStateFlow(GenerativeParameters())
    val parameters = _parameters.asStateFlow()

    fun onMealTypeChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                mealType = if (newValue == _parameters.value.mealType) "" else newValue,
            )
    }

    fun onCuisineChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                cuisine = if (newValue == _parameters.value.cuisine) "" else newValue,
            )
    }

    fun onMoodChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                mood = if (newValue == _parameters.value.mood) "" else newValue,
            )
    }

    fun onDietaryChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                dietary = if (newValue == _parameters.value.dietary) "" else newValue,
            )
    }

    fun isQuickMealChange(newValue: Boolean) {
        _parameters.value =
            _parameters.value.copy(
                isQuick = newValue,
            )
    }

    fun generateSuggestions(onSuccess: () -> Unit) {
        if (
            _parameters.value.mealType.isEmpty() ||
            _parameters.value.mood.isEmpty() ||
            _parameters.value.cuisine.isEmpty()
        ) {
            _genState.value =
                _genState.value.copy(
                    errorMessage = "Please select required",
                )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _genState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = "",
                )
            }

            try {
                val response =
                    generativeModel.generateContent(
                        content {
                            text(
                                PromptUtil.generativePrompt(
                                    recipeData = aiData.recipes,
                                    pastMeal = aiData.pastMeals,
                                    parameters = _parameters.value,
                                ),
                            )
                        },
                    )

                response.text?.let { modelResponse ->
                    _genState.update {
                        it.copy(
                            isLoading = false,
                            generativeAnswer = modelResponse,
                        )
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                _genState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Something went wrong!",
                    )
                }
            }
        }
    }
}
