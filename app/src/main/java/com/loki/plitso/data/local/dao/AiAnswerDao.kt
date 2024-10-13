package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.AiAnswer
import kotlinx.coroutines.flow.Flow

@Dao
interface AiAnswerDao : BaseDao<AiAnswer> {

    @Query("SELECT * FROM answers")
    fun getAnswers(): Flow<List<AiAnswer>>
}