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

fun RecipeDetail.toBookmark(): Bookmark {
    return Bookmark(recipeId, title, image, country, instructions, ingredients)
}

fun RecipeDetailDto.toDayRecipe(updateDate: Date): DayRecipe {
    return DayRecipe(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        updatedDate = updateDate
    )
}

fun RecipeDetail.toRandom(): Random {
    return Random(
        recipeId = recipeId,
        title = title,
        image = image,
        instructions = instructions
    )
}

fun Random.toDayRecipe(updateDate: Date): DayRecipe {
    return DayRecipe(
        recipeId = recipeId,
        title = title,
        image = image,
        updatedDate = updateDate
    )
}

fun CategoryDto.toCategory(): Category {
    return Category(
        categoryId = idCategory,
        title = strCategory,
        image = strCategoryThumb
    )
}

fun RecipeDto.toRecipe(categoryId: String): Recipe {
    return Recipe(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        categoryId = categoryId
    )
}

fun RecipeDetailDto.toRecipeDetail(): RecipeDetail {
    return RecipeDetail(
        recipeId = idMeal,
        title = strMeal,
        image = strMealThumb,
        country = strArea,
        instructions = strInstructions ?: "",
        ingredients = sanitizeIngredients(this)
    )
}

fun sanitizeIngredients(detailDto: RecipeDetailDto): List<String> {
    val ingredientList = mutableListOf<String>()

    detailDto.strMeasure1?.let {
        val ingredient = it + " ${detailDto.strIngredient1}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure2?.let {
        val ingredient = it + " ${detailDto.strIngredient2}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure3?.let {
        val ingredient = it + " ${detailDto.strIngredient3}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure4?.let {
        val ingredient = it + " ${detailDto.strIngredient4}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure5?.let {
        val ingredient = it + " ${detailDto.strIngredient5}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure6?.let {
        val ingredient = it + " ${detailDto.strIngredient6}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure7?.let {
        val ingredient = it + " ${detailDto.strIngredient7}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure8?.let {
        val ingredient = it + " ${detailDto.strIngredient8}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure9?.let {
        val ingredient = it + " ${detailDto.strIngredient9}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure10?.let {
        val ingredient = it + " ${detailDto.strIngredient10}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure11?.let {
        val ingredient = it + " ${detailDto.strIngredient11}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure12?.let {
        val ingredient = it + " ${detailDto.strIngredient12}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure13?.let {
        val ingredient = it + " ${detailDto.strIngredient13}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure14?.let {
        val ingredient = it + " ${detailDto.strIngredient14}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure15?.let {
        val ingredient = it + " ${detailDto.strIngredient15}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure16?.let {
        val ingredient = it + " ${detailDto.strIngredient16}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure17?.let {
        val ingredient = it + " ${detailDto.strIngredient17}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure18?.let {
        val ingredient = it + " ${detailDto.strIngredient8}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure19?.let {
        val ingredient = it + " ${detailDto.strIngredient9}"
        ingredientList.add(ingredient)
    }
    detailDto.strMeasure20?.let {
        val ingredient = it + " ${detailDto.strIngredient20}"
        ingredientList.add(ingredient)
    }

    return ingredientList
}