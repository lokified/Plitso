package com.loki.plitso.presentation.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.loki.plitso.data.local.dao.AiAnswerDao
import com.loki.plitso.data.local.models.AiAnswer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AiViewModel(
    private val aiAnswerDao: AiAnswerDao,
    private val generativeModel: GenerativeModel,
    val aiData: AiData,
) : ViewModel() {
    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    private val _parameters = MutableStateFlow(GenerativeParameters())
    val parameters = _parameters.asStateFlow()

    init {
        getMessages()
    }

    fun onMealTypeChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                mealType = newValue,
            )
    }

    fun onCuisineChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                cuisine = newValue,
            )
    }

    fun onMoodChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                mood = newValue,
            )
    }

    fun onDietaryChange(newValue: String) {
        _parameters.value =
            _parameters.value.copy(
                dietary = newValue,
            )
    }

    fun isQuickMealChange(newValue: Boolean) {
        _parameters.value =
            _parameters.value.copy(
                isQuick = newValue,
            )
    }

    private fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            aiAnswerDao.getAnswers().collect { data ->
                _state.update {
                    it.copy(
                        messages = data,
                    )
                }
            }
        }
    }

    fun askQuestion(question: String) {
        viewModelScope.launch(Dispatchers.IO) {
            aiAnswerDao.insert(
                AiAnswer(
                    role = "user",
                    content = question,
                ),
            )

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = "",
                )
            }

            try {
                val chat =
                    generativeModel.startChat(
                        history =
                            _state.value.messages.map { value ->
                                content(role = value.role) { text(value.content) }
                            },
                    )

                val response = chat.sendMessage(question + PromptUtil.OUT_OF_CONTEXT_WARNING)

                response.text?.let { modelResponse ->
                    aiAnswerDao.insert(
                        AiAnswer(
                            content = modelResponse,
                            role = "model",
                        ),
                    )
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Something went wrong!",
                    )
                }
            }
        }
    }

    fun generateSuggestions(onSuccess: () -> Unit) {
        if (
            _parameters.value.mealType.isEmpty() ||
            _parameters.value.mood.isEmpty() ||
            _parameters.value.cuisine.isEmpty()
        ) {
            _state.value =
                _state.value.copy(
                    errorMessage = "Please select required",
                )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
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
                    _state.update {
                        it.copy(
                            isLoading = false,
                            generativeAnswer = modelResponse,
                        )
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Something went wrong!",
                    )
                }
            }
        }
    }
}
