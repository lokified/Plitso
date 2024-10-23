package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_documentations")
data class FoodDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val servedOn: String,
    val description: String,
    val picture: String? = null,
    val userID: String? = null,
)
