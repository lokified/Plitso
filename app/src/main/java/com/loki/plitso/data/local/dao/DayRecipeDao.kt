package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.DayRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface DayRecipeDao : BaseDao<DayRecipe> {
    @Query("SELECT * FROM dayrecipe")
    fun getRecipes(): Flow<List<DayRecipe>>

    @Query("DELETE FROM dayrecipe")
    suspend fun clear()
}
