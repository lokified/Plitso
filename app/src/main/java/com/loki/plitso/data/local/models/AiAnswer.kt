package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = ChatHistory::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class AiAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: String,
    val content: String,
    val chatId: UUID,
)
