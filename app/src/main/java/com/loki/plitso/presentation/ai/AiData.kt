package com.loki.plitso.presentation.ai

import com.loki.plitso.data.local.dao.FoodDocumentDao
import com.loki.plitso.data.local.dao.RecipeDetailDao
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.data.local.models.RecipeDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiData(
    private val recipeDetailDao: RecipeDetailDao,
    private val foodDocumentDao: FoodDocumentDao,
) {
    val recipes = mutableListOf<RecipeData>()
    val countries = mutableListOf<String>()
    val pastMeals = mutableListOf<PastMeal>()

    init {
        getPastMeals()
        getRecipes()
        getCountries()
    }

    private fun getPastMeals() {
        CoroutineScope(Dispatchers.IO).launch {
            foodDocumentDao.getFoodDocuments().collect { data ->
                pastMeals.addAll(data.map { it.toPastMeal() })
            }
        }
    }

    private fun getCountries() {
        CoroutineScope(Dispatchers.IO).launch {
            recipeDetailDao.getCountries().collect { data ->
                countries.addAll(data.filter { it != "Unknown" }.distinct())
            }
        }
    }

    private fun getRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            recipeDetailDao.getAllRecipes().collect { recipeDetails ->
                val data =
                    recipeDetails.map {
                        it.toRecipeData()
                    }
                recipes.addAll(data)
            }
        }
    }
}

data class PastMeal(
    val type: String,
    val servedOn: String,
    val description: String,
)

fun FoodDocument.toPastMeal(): PastMeal =
    PastMeal(
        type,
        servedOn,
        description,
    )

data class RecipeData(
    val title: String,
    val country: String,
    val instructions: String,
    val ingredients: String,
)

fun RecipeDetail.toRecipeData(): RecipeData =
    RecipeData(
        title,
        country,
        instructions,
        ingredients.joinToString(" "),
    )
