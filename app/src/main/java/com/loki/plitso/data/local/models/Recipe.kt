package com.loki.plitso.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val recipeId: String,
    val title: String,
    val image: String,
    val categoryId: String
)

data class CategoryWithRecipes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val recipes: List<Recipe>
)