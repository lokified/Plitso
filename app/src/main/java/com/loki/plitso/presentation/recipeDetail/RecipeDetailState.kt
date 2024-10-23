package com.loki.plitso.presentation.recipeDetail

import com.loki.plitso.data.local.models.RecipeDetail

data class RecipeDetailState(
    val isLoading: Boolean = false,
    val message: String = "",
    val recipeDetail: RecipeDetail? = null,
    val isBookmarked: Boolean = false,
)
