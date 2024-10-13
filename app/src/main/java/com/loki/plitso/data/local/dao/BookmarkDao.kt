package com.loki.plitso.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.loki.plitso.data.local.models.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao: BaseDao<Bookmark> {

    @Query("SELECT * FROM bookmarks WHERE recipeId = :id")
    fun getBookmark(id: String): Flow<Bookmark?>

    @Query("SELECT * FROM bookmarks")
    fun getBookmarks(): Flow<List<Bookmark>>
}