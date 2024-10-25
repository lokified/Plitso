package com.loki.plitso.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.RandomDao
import com.loki.plitso.data.repository.recipe.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val recipeRepository: RecipeRepository,
    private val randomDao: RandomDao,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val dayRecipe =
        recipeRepository.getDayRecipe().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null,
        )

    init {
        showRandomRecipe()
        getCategories()
    }

    private fun showRandomRecipe() {
        viewModelScope.launch {
            recipeRepository.generateRandomRecipe()

            randomDao.getRandomRecipe().collect { random ->
                if (random.isNotEmpty()) {
                    _state.value =
                        _state.value.copy(
                            randomRecipeImage = random[0].image,
                            randomRecipeId = random[0].recipeId,
                        )
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            recipeRepository.categories.collect { categories ->
                _state.value =
                    _state.value.copy(
                        categories = categories,
                    )
            }
        }
    }
}
