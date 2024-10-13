package com.loki.plitso.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.RecipeDetailDao
import com.loki.plitso.data.local.models.RecipeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val recipeDetailDao: RecipeDetailDao
): ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeDetail>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _searchTerm = mutableStateOf("")
    val searchTerm: State<String> = _searchTerm

    fun onSearchTermChange(newValue: String) {
        _searchTerm.value = newValue
        if (_searchTerm.value.isEmpty()) {
            _recipes.value = emptyList()
        } else {
            searchRecipe(newValue)
        }
    }

    private fun searchRecipe(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeDetailDao.searchRecipe(searchTerm).collect { data ->
                _recipes.update {
                    data
                }
            }
        }
    }
}