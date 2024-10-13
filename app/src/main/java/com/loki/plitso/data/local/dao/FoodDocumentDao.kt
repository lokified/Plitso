package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.FoodDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDocumentDao: BaseDao<FoodDocument> {

    @Query("SELECT * FROM food_documentations ORDER BY id DESC")
    fun getFoodDocuments(): Flow<List<FoodDocument>>
}