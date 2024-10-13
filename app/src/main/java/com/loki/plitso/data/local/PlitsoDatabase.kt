package com.loki.plitso.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loki.plitso.data.local.dao.AiAnswerDao
import com.loki.plitso.data.local.dao.BookmarkDao
import com.loki.plitso.data.local.dao.CategoryDao
import com.loki.plitso.data.local.dao.DayRecipeDao
import com.loki.plitso.data.local.dao.FoodDocumentDao
import com.loki.plitso.data.local.dao.RandomDao
import com.loki.plitso.data.local.dao.RecipeDao
import com.loki.plitso.data.local.dao.RecipeDetailDao
import com.loki.plitso.data.local.models.AiAnswer
import com.loki.plitso.data.local.models.Bookmark
import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.data.local.models.Random
import com.loki.plitso.data.local.models.Recipe
import com.loki.plitso.data.local.models.RecipeDetail

@Database(
    exportSchema = false,
    version = 3,
    entities = [
        DayRecipe::class,
        Category::class,
        Recipe::class,
        RecipeDetail::class,
        Random::class,
        Bookmark::class,
        FoodDocument::class,
        AiAnswer::class
    ]
)
@TypeConverters(Converters::class)
abstract class PlitsoDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao
    abstract val recipeDao: RecipeDao
    abstract val dayRecipeDao: DayRecipeDao
    abstract val recipeDetailDao: RecipeDetailDao
    abstract val randomDao: RandomDao
    abstract val bookmarkDao: BookmarkDao
    abstract val foodDocumentDao: FoodDocumentDao
    abstract val aiAnswerDao: AiAnswerDao

    companion object {
        const val DATABASE_NAME = "plitso_db"
    }
}