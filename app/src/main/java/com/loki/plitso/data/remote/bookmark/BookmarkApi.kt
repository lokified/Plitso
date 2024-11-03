package com.loki.plitso.data.remote.bookmark

import com.loki.plitso.data.local.models.Bookmark

interface BookmarkApi {

    fun saveBookmark(bookmark: Bookmark): Boolean

    fun deleteBookmark(recipeId: String)

    fun isBookmarked(recipeId: String): BookmarkApi
}
