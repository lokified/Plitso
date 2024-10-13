package com.loki.plitso.presentation.recipe_detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.BookmarkDao
import com.loki.plitso.data.local.models.Bookmark
import com.loki.plitso.data.remote.mealdb.mappers.toBookmark
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.util.Constants.RECIPE_ID_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RecipeDetailViewModel(
    private val recipeRepository: RecipeRepository,
    private val savedStateHandle: SavedStateHandle,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailState())
    val state = _state.asStateFlow()

    private val recipe
        get() = _state.value.recipeDetail

    private val bookmarked = mutableStateOf<Bookmark?>(null)

    init {
        savedStateHandle.get<String>(RECIPE_ID_KEY)?.let { id ->
            getIsBookmarked(id)
            getRecipeDetail(id)
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            if (_state.value.isBookmarked) {
                bookmarkDao.delete(recipe!!.toBookmark())
                _state.value = _state.value.copy(
                    isBookmarked = false
                )
            } else {
                bookmarkDao.insert(recipe!!.toBookmark())
                _state.value = _state.value.copy(
                    isBookmarked = true
                )
            }
        }
    }

    private fun getRecipeDetail(id: String) {
        viewModelScope.launch {
            try {
                recipeRepository.getRecipeDetail(id).collect { recipe ->
                    _state.value = _state.value.copy(
                        recipeDetail = recipe,
                        isBookmarked = bookmarked.value?.recipeId == recipe.recipeId
                    )
                }
            } catch (e: Exception) {
                Timber.tag("vm recipe detail").d(e)
            }
        }
    }

    private fun getIsBookmarked(id: String) {
        viewModelScope.launch {
            try {
                bookmarkDao.getBookmark(id).collect {
                    bookmarked.value = it
                }
            } catch (e: Exception) {
                Timber.tag("vm recipe bookmark").d(e)
            }
        }
    }
}