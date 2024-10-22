package com.loki.plitso.presentation.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.RecipeDetailDao
import com.loki.plitso.data.local.models.RecipeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val recipeDetailDao: RecipeDetailDao
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeDetail>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    private val filtered = mutableStateListOf<String>()

    @OptIn(FlowPreview::class)
    fun onSearchTermChange(newValue: String) {
        _searchTerm.value = newValue

        viewModelScope.launch {
            _searchTerm
                .debounce(300L)
                .collect { term ->
                    searchRecipe(term)
                }
        }
    }

    fun onFilteredChange(filter: String) {
        if (!filtered.contains(filter)) {
            filtered.add(filter)
        } else {
            filtered.remove(filter)
        }
        searchRecipe(searchTerm.value)
    }

    private fun searchRecipe(searchTerm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = if (filtered.isEmpty()) {
                recipeDetailDao.searchRecipe(searchTerm)
            } else {
                recipeDetailDao.searchRecipe(
                    title = if (filtered.contains("title")) searchTerm else null,
                    cuisine = if (filtered.contains("cuisine")) searchTerm else null,
                    ingredient = if (filtered.contains("ingredient")) searchTerm else null
                )
            }

            response.collect { data ->
                if (_searchTerm.value.isEmpty()) {
                    _recipes.update {
                        emptyList()
                    }
                } else {
                    _recipes.update {
                        data
                    }
                }
            }
        }
    }
}