package com.loki.plitso.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.RandomDao
import com.loki.plitso.data.local.datastore.DatastoreStorage
import com.loki.plitso.data.local.datastore.LocalUser
import com.loki.plitso.data.remote.mealdb.mappers.toRandom
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val recipeRepository: RecipeRepository,
    private val randomDao: RandomDao,
    private val datastoreStorage: DatastoreStorage
) : ViewModel() {

    val user = datastoreStorage.getLocalUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        LocalUser()
    )

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val dayRecipe = recipeRepository.getDayRecipe().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        null
    )

    init {
        getRandomRecipe()
        showRandomRecipe()
        getCategories()
    }

    private fun showRandomRecipe() {
        viewModelScope.launch {
            randomDao.getRandomRecipe().collect { random ->
                if (random.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        randomRecipeImage = random[0].image,
                        randomRecipeId = random[0].recipeId
                    )
                }
            }
        }
    }

    private fun getRandomRecipe() {
        viewModelScope.launch {
            recipeRepository.getRandomRecipe().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Timber.tag("random recipe").d(result.message)
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        randomDao.clear()
                        randomDao.insert(result.data.toRandom())
                    }
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            recipeRepository.categories.collect { categories ->
                _state.value = _state.value.copy(
                    categories = categories
                )
            }
        }
    }
}