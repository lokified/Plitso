package com.loki.plitso.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.BookmarkDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BookmarkViewmodel(
    private val bookmarkDao: BookmarkDao
): ViewModel() {

    val bookmarks = bookmarkDao.getBookmarks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )
}