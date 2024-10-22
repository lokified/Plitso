package com.loki.plitso.data.remote.mealdb.mappers

import com.loki.plitso.data.local.models.Bookmark
import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe
import com.loki.plitso.data.local.models.Random
import com.loki.plitso.data.local.models.Recipe
import com.loki.plitso.data.local.models.RecipeDetail
import com.loki.plitso.data.remote.mealdb.models.CategoryDto
import com.loki.plitso.data.remote.mealdb.models.RecipeDetailDto
import com.loki.plitso.data.remote.mealdb.models.RecipeDto
import java.util.Date

fun RecipeDetail.toBookmark(): Bookmark =
    Bookmark(recipeId, title, image, country, instructions, ingredients)

fun RecipeDetailDto.toDayRecipe(updateDate: Date): DayRecipe =
    DayRecipe(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        updatedDate = updateDate,
    )

fun RecipeDetail.toRandom(): Random =
    Random(
        recipeId = recipeId,
        title = title,
        image = image,
        instructions = instructions,
    )

fun Random.toDayRecipe(updateDate: Date): DayRecipe =
    DayRecipe(
        recipeId = recipeId,
        title = title,
        image = image,
        updatedDate = updateDate,
    )

fun CategoryDto.toCategory(): Category =
    Category(
        categoryId = idCategory,
        title = strCategory,
        image = strCategoryThumb,
    )

fun RecipeDto.toRecipe(categoryId: String): Recipe =
    Recipe(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        categoryId = categoryId,
    )

fun RecipeDetailDto.toRecipeDetail(): RecipeDetail =
    RecipeDetail(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        country = strArea,
        instructions = strInstructions ?: "",
        ingredients = sanitizeIngredients(this),
    )

fun sanitizeIngredients(detailDto: RecipeDetailDto): List<String> {
    val ingredientList = mutableListOf<String>()

    for (i in 1..20) {
        val measure =
            detailDto::class.members.find {
                it.name == "strMeasure$i"
            }?.call(detailDto) as? String
        val ingredient =
            detailDto::class.members.find {
                it.name == "strIngredient$i"
            }?.call(detailDto) as? String

        if (!measure.isNullOrBlank() && !ingredient.isNullOrBlank()) {
            ingredientList.add("$measure $ingredient")
        }
    }

    return ingredientList
}
