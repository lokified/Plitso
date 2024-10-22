package com.loki.plitso.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.presentation.account.AccountScreen
import com.loki.plitso.presentation.account.AccountViewModel
import com.loki.plitso.presentation.ai.AIScreen
import com.loki.plitso.presentation.ai.AiViewModel
import com.loki.plitso.presentation.bookmark.BookmarkScreen
import com.loki.plitso.presentation.bookmark.BookmarkViewmodel
import com.loki.plitso.presentation.document.DocumentScreen
import com.loki.plitso.presentation.document.DocumentViewModel
import com.loki.plitso.presentation.food_documents.FoodDocumentsScreen
import com.loki.plitso.presentation.home.HomeScreen
import com.loki.plitso.presentation.home.HomeViewModel
import com.loki.plitso.presentation.login.LoginScreen
import com.loki.plitso.presentation.login.LoginViewModel
import com.loki.plitso.presentation.recipe_detail.RecipeDetailScreen
import com.loki.plitso.presentation.recipe_detail.RecipeDetailViewModel
import com.loki.plitso.presentation.recipes.RecipesScreen
import com.loki.plitso.presentation.recipes.RecipesViewModel
import com.loki.plitso.presentation.search.SearchScreen
import com.loki.plitso.presentation.search.SearchViewModel
import com.loki.plitso.util.Constants.CATEGORY_ID_KEY
import com.loki.plitso.util.Constants.CATEGORY_NAME_KEY
import com.loki.plitso.util.Constants.RECIPE_ID_KEY
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    appState: AppState,
    plitsoViewModel: PlitsoViewModel
) {

    val documentViewModel = koinViewModel<DocumentViewModel>()
    val foodDocuments by documentViewModel.foodDocuments.collectAsStateWithLifecycle()

    SharedTransitionLayout {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.HomeScreen.route
        ) {
            composable(Screen.HomeScreen.route) {
                val homeViewModel = koinViewModel<HomeViewModel>()
                HomeScreen(
                    animatedVisibilityScope = this,
                    homeViewModel = homeViewModel,
                    plitsoViewModel = plitsoViewModel,
                    navigateToAccountScreen = {
                        appState.navigate(Screen.AccountScreen)
                    },
                    navigateToSearchScreen = {
                        appState.navigate(Screen.SearchScreen)
                    },
                    navigateToRecipesScreen = { category ->
                        appState.navigate(Screen.RecipesScreen.navWith("${category.categoryId}/${category.title}"))
                    },
                    navigateToRecipeDetailScreen = { id ->
                        if (id.isNotEmpty()) {
                            appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                        }
                    }
                )
            }

            composable(
                route = Screen.RecipesScreen.withCategory(),
                arguments = listOf(
                    navArgument(CATEGORY_ID_KEY) {
                        type = NavType.StringType
                    },
                    navArgument(CATEGORY_NAME_KEY) {
                        type = NavType.StringType
                    }
                )
            ) {
                val recipesViewModel =
                    koinViewModel<RecipesViewModel> { parametersOf(SavedStateHandle()) }
                val recipesState by recipesViewModel.state.collectAsStateWithLifecycle()
                RecipesScreen(
                    animatedVisibilityScope = this,
                    recipesState = recipesState,
                    navigateToRecipeDetailScreen = { id ->
                        appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                    },
                    navigateBack = appState::navigateUp
                )
            }

            composable(
                route = Screen.RecipeDetailScreen.withRecipeId(),
                arguments = listOf(
                    navArgument(RECIPE_ID_KEY) {
                        type = NavType.StringType
                    }
                )
            ) {
                val recipeDetailViewModel =
                    koinViewModel<RecipeDetailViewModel> { parametersOf(SavedStateHandle()) }
                val recipeDetailState by recipeDetailViewModel.state.collectAsStateWithLifecycle()
                RecipeDetailScreen(
                    animatedVisibilityScope = this,
                    recipeDetailState = recipeDetailState,
                    navigateBack = appState::navigateUp,
                    onAddToBookmark = recipeDetailViewModel::toggleBookmark
                )
            }

            composable(
                route = Screen.SearchScreen.route
            ) {
                val searchViewModel = koinViewModel<SearchViewModel>()
                SearchScreen(
                    animatedVisibilityScope = this,
                    searchViewModel = searchViewModel,
                    navigateToRecipeDetailScreen = { id ->
                        appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                    }
                )
            }

            composable(Screen.DocumentScreen.route) {
                DocumentScreen(
                    foodDocuments = foodDocuments,
                    documentViewModel = documentViewModel,
                    navigateToAllFoodDocument = {
                        appState.navigate(Screen.FoodDocumentsScreen)
                    }
                )
            }

            composable(Screen.FoodDocumentsScreen.route) {
                FoodDocumentsScreen(
                    foodDocuments = foodDocuments,
                    documentViewModel = documentViewModel,
                    navigateBack = appState::navigateUp
                )
            }

            composable(Screen.AIScreen.route) {
                val aiViewModel = koinViewModel<AiViewModel>()
                AIScreen(
                    aiViewModel = aiViewModel,
                    plitsoViewModel = plitsoViewModel,
                    navigateToLogin = {
                        appState.navigate(Screen.LoginScreen)
                    },
                    navigateBack = appState::navigateUp
                )
            }

            composable(Screen.BookmarkScreen.route) {
                val bookmarkViewModel = koinViewModel<BookmarkViewmodel>()
                val bookmarks by bookmarkViewModel.bookmarks.collectAsStateWithLifecycle()
                BookmarkScreen(
                    animatedVisibilityScope = this,
                    bookmarks = bookmarks,
                    navigateToRecipeDetailScreen = { id ->
                        appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                    }
                )
            }

            composable(Screen.AccountScreen.route) {
                val accountViewModel = koinViewModel<AccountViewModel>()
                val accountState by accountViewModel.state.collectAsStateWithLifecycle()
                AccountScreen(
                    plitsoViewModel = plitsoViewModel,
                    accountState = accountState,
                    onThemeChange = accountViewModel::onChangeTheme,
                    onLogOut = accountViewModel::logOut,
                    navigateToSignIn = {
                        appState.navigate(Screen.LoginScreen)
                    }
                )
            }

            composable(Screen.LoginScreen.route) {
                val loginViewModel = koinViewModel<LoginViewModel>()
                LoginScreen(
                    loginViewModel = loginViewModel,
                    navigateBack = appState::navigateUp
                )
            }
        }
    }
}