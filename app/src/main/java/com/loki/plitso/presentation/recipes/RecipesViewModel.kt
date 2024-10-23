package com.loki.plitso.presentation.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.util.Constants.CATEGORY_ID_KEY
import com.loki.plitso.util.Constants.CATEGORY_NAME_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RecipesViewModel(
    private val recipeRepository: RecipeRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(RecipesState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>(CATEGORY_NAME_KEY)?.let { name ->
            _state.value =
                _state.value.copy(
                    category = name,
                )
        }
        savedStateHandle.get<String>(CATEGORY_ID_KEY)?.let { category ->
            getRecipes(category)
        }
    }

    private fun getRecipes(categoryId: String) {
        viewModelScope.launch {
            try {
                val recipes = recipeRepository.getRecipes(categoryId)
                _state.value =
                    _state.value.copy(
                        recipes = recipes,
                    )
            } catch (e: Exception) {
                Timber.tag("vm recipes").d(e)
            }
        }
    }
}
