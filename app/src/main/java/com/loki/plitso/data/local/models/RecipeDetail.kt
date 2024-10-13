package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_detail")
data class RecipeDetail(
    @PrimaryKey
    val recipeId: String,
    val title: String,
    val image: String,
    val country: String,
    val instructions: String,
    val ingredients: List<String>
)