package com.loki.plitso.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val categoryId: String,
    val title: String,
    val image: String,
)
