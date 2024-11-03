package com.loki.plitso.data.remote.bookmark

import com.loki.plitso.data.local.models.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkApi {

    val bookmarks: Flow<List<String>>

    suspend fun saveBookmark(recipeId: String): Boolean

    suspend fun deleteBookmark(recipeId: String)

    suspend fun isBookmarked(recipeId: String): Boolean
}
