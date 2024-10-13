package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DayRecipe(
    @PrimaryKey
    val recipeId: String,
    val title: String,
    val image: String,
    val updatedDate: Date
)