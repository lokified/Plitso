package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "random_recipe")
data class Random(
    @PrimaryKey
    val recipeId: String,
    val title: String,
    val image: String,
    val instructions: String,
)
