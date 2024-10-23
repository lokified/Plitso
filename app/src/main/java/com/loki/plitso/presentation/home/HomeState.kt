package com.loki.plitso.presentation.home

import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe

data class HomeState(
    val dayRecipe: DayRecipe? = null,
    val randomRecipeId: String = "",
    val randomRecipeImage: String = "",
    val categories: List<Category> = emptyList(),
)
