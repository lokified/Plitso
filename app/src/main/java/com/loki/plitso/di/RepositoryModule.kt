package com.loki.plitso.di

import androidx.lifecycle.SavedStateHandle
import com.loki.plitso.data.local.PlitsoDatabase
import com.loki.plitso.data.repository.recipe.RecipeRepository
import com.loki.plitso.data.repository.recipe.RecipeRepositoryImpl
import com.loki.plitso.presentation.account.AccountViewModel
import com.loki.plitso.presentation.bookmark.BookmarkViewmodel
import com.loki.plitso.presentation.document.DocumentViewModel
import com.loki.plitso.presentation.home.HomeViewModel
import com.loki.plitso.presentation.recipe_detail.RecipeDetailViewModel
import com.loki.plitso.presentation.recipes.RecipesViewModel
import com.loki.plitso.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { get<PlitsoDatabase>().categoryDao }
    single { get<PlitsoDatabase>().recipeDao }
    single { get<PlitsoDatabase>().recipeDetailDao }
    single { get<PlitsoDatabase>().dayRecipeDao }
    single { get<PlitsoDatabase>().randomDao }
    single { get<PlitsoDatabase>().bookmarkDao }
    single { get<PlitsoDatabase>().foodDocumentDao }

    single<RecipeRepository> { RecipeRepositoryImpl(get(), get(), get(), get(), get()) }

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { (handle: SavedStateHandle) -> RecipesViewModel(get(), handle) }
    viewModel { (handle: SavedStateHandle) -> RecipeDetailViewModel(get(), handle, get()) }
    viewModel { BookmarkViewmodel(get()) }
    viewModel { DocumentViewModel(get()) }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
}