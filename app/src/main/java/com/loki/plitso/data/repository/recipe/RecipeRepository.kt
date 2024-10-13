package com.loki.plitso.data.repository.recipe

import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe
import com.loki.plitso.data.local.models.Recipe
import com.loki.plitso.data.local.models.RecipeDetail
import com.loki.plitso.data.remote.mealdb.models.RecipeDetailDto
import com.loki.plitso.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    val categories: Flow<List<Category>>
    fun getDayRecipe(): Flow<DayRecipe>
    fun getRecipeDetail(id: String): Flow<RecipeDetail>
    suspend fun getRecipes(categoryId: String): List<Recipe>
    fun getRandomRecipe(): Flow<Resource<RecipeDetailDto>>
    suspend fun refreshDatabase()
}