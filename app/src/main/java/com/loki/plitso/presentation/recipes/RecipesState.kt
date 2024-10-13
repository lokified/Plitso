package com.loki.plitso.presentation.recipes

import com.loki.plitso.data.local.models.Recipe

data class RecipesState(
    val isLoading: Boolean = false,
    val message: String = "",
    val category: String = "",
    val recipes: List<Recipe> = emptyList()
)
