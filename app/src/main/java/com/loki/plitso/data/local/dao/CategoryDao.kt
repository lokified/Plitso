package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.CategoryWithRecipes
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<Category> {
    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategoriesWithRecipes(categoryId: String): CategoryWithRecipes
}
