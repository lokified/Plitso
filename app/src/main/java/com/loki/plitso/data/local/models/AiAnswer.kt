package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AiAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: String,
    val content: String,
)
