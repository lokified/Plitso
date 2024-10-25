package com.loki.plitso.data.repository.recipe

import com.loki.plitso.data.local.dao.CategoryDao
import com.loki.plitso.data.local.dao.DayRecipeDao
import com.loki.plitso.data.local.dao.RandomDao
import com.loki.plitso.data.local.dao.RecipeDao
import com.loki.plitso.data.local.dao.RecipeDetailDao
import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe
import com.loki.plitso.data.local.models.Random
import com.loki.plitso.data.local.models.Recipe
import com.loki.plitso.data.local.models.RecipeDetail
import com.loki.plitso.data.remote.mealdb.MealdbApi
import com.loki.plitso.data.remote.mealdb.mappers.toCategory
import com.loki.plitso.data.remote.mealdb.mappers.toDayRecipe
import com.loki.plitso.data.remote.mealdb.mappers.toRandom
import com.loki.plitso.data.remote.mealdb.mappers.toRecipe
import com.loki.plitso.data.remote.mealdb.mappers.toRecipeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

class RecipeRepositoryImpl(
    private val api: MealdbApi,
    private val categoryDao: CategoryDao,
    private val recipeDao: RecipeDao,
    private val recipeDetailDao: RecipeDetailDao,
    private val dayRecipeDao: DayRecipeDao,
    private val randomDao: RandomDao,
) : RecipeRepository {
    override fun getDayRecipe(): Flow<DayRecipe> =
        flow {
            try {
                val recipe = dayRecipeDao.getRecipes().first()
                if (recipe.isEmpty()) {
                    val randomRecipe = api.getRandomRecipe().meals[0]
                    dayRecipeDao.insert(randomRecipe.toDayRecipe(Date()))
                    val dayRecipe = dayRecipeDao.getRecipes().first()[0]
                    emit(dayRecipe)
                } else {
                    val dayRecipe = dayRecipeDao.getRecipes().first()[0]
                    emit(dayRecipe)
                }
            } catch (e: Exception) {
                Timber.tag("day recipe repo").d(e)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun generateRandomRecipe() {
        withContext(Dispatchers.IO) {
            try {
                val random = randomDao.getRandomRecipe().first()
                if (random.isEmpty()) {
                    val randomRecipe = api.getRandomRecipe().meals[0]
                    randomDao.insert(randomRecipe.toRandom())
                } else {
                    recipeDetailDao.getAllRecipes().firstOrNull()?.let {
                        randomDao.clear()
                        val randomIndex = kotlin.random.Random.nextInt(it.size)
                        randomDao.insert(it[randomIndex].toRandom())
                    }
                }
            } catch (e: Exception) {
                Timber.tag("random recipe repo").d(e)
            }
        }
    }

    override val categories: Flow<List<Category>>
        get() = categoryDao.getCategories()

    override fun getRecipeDetail(id: String): Flow<RecipeDetail> =
        recipeDetailDao.getRecipeDetail(id)

    override suspend fun getRecipes(categoryId: String): List<Recipe> =
        categoryDao.getCategoriesWithRecipes(categoryId).recipes

    override suspend fun refreshDatabase() {
        withContext(Dispatchers.IO) {
            try {
                val categories = api.getCategories().categories
                categoryDao.insert(categories.map { it.toCategory() })

                for (category in categories) {
                    val title = category.strCategory
                    val recipes = api.getCategoryRecipe(title).meals

                    recipeDao.insert(recipes.map { it.toRecipe(category.idCategory) })

                    for (recipe in recipes) {
                        val recipeDetail = api.getRecipeDetail(recipe.idMeal).meals
                        recipeDetailDao.insert(recipeDetail.map { it.toRecipeDetail() })
                    }
                }
            } catch (e: Exception) {
                Timber.tag("API ERR").d(e)
            }
        }
    }
}
