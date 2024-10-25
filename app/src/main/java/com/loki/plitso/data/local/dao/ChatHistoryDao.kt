package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.ChatHistory
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ChatHistoryDao : BaseDao<ChatHistory> {
    @Query("SELECT * FROM chat_history")
    fun getAllChats(): Flow<List<ChatHistory>>

    @Query("SELECT * FROM chat_history WHERE id = :id")
    fun getChat(id: UUID): Flow<ChatHistory>

    @Query("DELETE FROM chat_history WHERE id = :id")
    suspend fun deleteChat(id: UUID)
}
