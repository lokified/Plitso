package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.Random
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomDao : BaseDao<Random> {
    @Query("SELECT * FROM random_recipe")
    fun getRandomRecipe(): Flow<List<Random>>

    @Query("DELETE FROM random_recipe")
    suspend fun clear()
}
