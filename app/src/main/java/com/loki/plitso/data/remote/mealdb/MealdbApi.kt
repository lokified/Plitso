package com.loki.plitso.data.remote.mealdb

import com.loki.plitso.data.remote.mealdb.models.CategoryResponse
import com.loki.plitso.data.remote.mealdb.models.RecipeDetailResponse
import com.loki.plitso.data.remote.mealdb.models.RecipesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealdbApi {

    @GET("random.php")
    suspend fun getRandomRecipe(): RecipeDetailResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getCategoryRecipe(@Query("c") category: String): RecipesResponse

    @GET("lookup.php")
    suspend fun getRecipeDetail(@Query("i") id: String): RecipeDetailResponse
}