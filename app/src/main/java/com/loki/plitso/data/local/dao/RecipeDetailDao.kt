package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.RecipeDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDetailDao : BaseDao<RecipeDetail> {
    @Query("SELECT * FROM recipe_detail WHERE recipeId = :recipeId")
    fun getRecipeDetail(recipeId: String): Flow<RecipeDetail>

    @Query("SELECT * FROM recipe_detail")
    fun getAllRecipes(): Flow<List<RecipeDetail>>

    @Query("SELECT country FROM recipe_detail")
    fun getCountries(): Flow<List<String>>

    @Query(
        """
        SELECT * FROM recipe_detail WHERE
        title LIKE '%' || :searchTerm || '%' 
        OR country LIKE '%' || :searchTerm || '%' 
        OR ingredients LIKE '%' || :searchTerm || '%'
    """,
    )
    fun searchRecipe(searchTerm: String): Flow<List<RecipeDetail>>

    @Query(
        """
        SELECT * FROM recipe_detail WHERE
        (:title IS NULL OR title LIKE '%' || :title || '%') AND
        (:cuisine IS NULL OR country LIKE '%' || :cuisine || '%') AND
        (:ingredient IS NULL OR ingredients LIKE '%' || :ingredient || '%')
    """,
    )
    fun searchRecipe(
        title: String?,
        cuisine: String?,
        ingredient: String?,
    ): Flow<List<RecipeDetail>>
}
