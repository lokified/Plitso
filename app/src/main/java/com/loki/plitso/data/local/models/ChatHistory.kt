package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "chat_history")
data class ChatHistory(
    @PrimaryKey
    val id: UUID,
    val title: String,
    val startedOn: Date,
)
