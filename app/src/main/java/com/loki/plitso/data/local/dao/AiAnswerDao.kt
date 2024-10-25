package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.AiAnswer
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface AiAnswerDao : BaseDao<AiAnswer> {
    @Query("SELECT * FROM answers")
    fun getAnswers(): Flow<List<AiAnswer>>

    @Query("SELECT * FROM answers WHERE chatId = :chatId")
    fun getAnswersByChat(chatId: UUID): Flow<List<AiAnswer>>
}
