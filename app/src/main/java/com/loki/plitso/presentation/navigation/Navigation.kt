package com.loki.plitso.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import com.loki.plitso.presentation.ai.chat.ChatScreen
import com.loki.plitso.presentation.ai.chat.ChatViewModel
import com.loki.plitso.presentation.ai.generative.GenerativeScreen
import com.loki.plitso.presentation.ai.generative.GenerativeViewModel
import com.loki.plitso.presentation.bookmark.BookmarkScreen
import com.loki.plitso.presentation.bookmark.BookmarkViewmodel
import com.loki.plitso.presentation.document.DocumentScreen
import com.loki.plitso.presentation.document.DocumentViewModel
import com.loki.plitso.presentation.foodDocuments.FoodDocumentsScreen
import com.loki.plitso.presentation.home.HomeScreen
import com.loki.plitso.presentation.home.HomeViewModel
import com.loki.plitso.presentation.login.LoginScreen
import com.loki.plitso.presentation.login.LoginViewModel
import com.loki.plitso.presentation.recipeDetail.RecipeDetailScreen
import com.loki.plitso.presentation.recipeDetail.RecipeDetailViewModel
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
    plitsoViewModel: PlitsoViewModel,
) {
    val documentViewModel = koinViewModel<DocumentViewModel>()
    val foodDocuments by documentViewModel.foodDocuments.collectAsStateWithLifecycle()

    SharedTransitionLayout {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.HomeScreen.route,
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
                        appState.navigate(
                            Screen.RecipesScreen.navWith(
                                "${category.categoryId}/${category.title}",
                            ),
                        )
                    },
                    navigateToRecipeDetailScreen = { id ->
                        if (id.isNotEmpty()) {
                            appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                        }
                    },
                )
            }

            composable(
                route = Screen.RecipesScreen.withCategory(),
                arguments =
                    listOf(
                        navArgument(CATEGORY_ID_KEY) {
                            type = NavType.StringType
                        },
                        navArgument(CATEGORY_NAME_KEY) {
                            type = NavType.StringType
                        },
                    ),
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(200),
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(200),
                    )
                },
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
                    navigateBack = appState::navigateUp,
                )
            }

            composable(
                route = Screen.RecipeDetailScreen.withRecipeId(),
                arguments =
                    listOf(
                        navArgument(RECIPE_ID_KEY) {
                            type = NavType.StringType
                        },
                    ),
            ) {
                val recipeDetailViewModel =
                    koinViewModel<RecipeDetailViewModel> { parametersOf(SavedStateHandle()) }
                val recipeDetailState by recipeDetailViewModel.state.collectAsStateWithLifecycle()
                RecipeDetailScreen(
                    animatedVisibilityScope = this,
                    recipeDetailState = recipeDetailState,
                    navigateBack = appState::navigateUp,
                    onAddToBookmark = recipeDetailViewModel::toggleBookmark,
                )
            }

            composable(
                route = Screen.SearchScreen.route,
            ) {
                val searchViewModel = koinViewModel<SearchViewModel>()
                SearchScreen(
                    animatedVisibilityScope = this,
                    searchViewModel = searchViewModel,
                    navigateToRecipeDetailScreen = { id ->
                        appState.navigate(Screen.RecipeDetailScreen.navWith(id))
                    },
                )
            }

            composable(Screen.DocumentScreen.route) {
                DocumentScreen(
                    foodDocuments = foodDocuments,
                    documentViewModel = documentViewModel,
                    navigateToAllFoodDocument = {
                        appState.navigate(Screen.FoodDocumentsScreen)
                    },
                )
            }

            composable(
                route = Screen.FoodDocumentsScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300),
                    )
                },
            ) {
                FoodDocumentsScreen(
                    foodDocuments = foodDocuments,
                    documentViewModel = documentViewModel,
                    navigateBack = appState::navigateUp,
                )
            }

            composable(
                route = Screen.AIScreen.route,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(300),
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(300),
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(300),
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(300),
                    )
                },
            ) {
                AIScreen(
                    plitsoViewModel = plitsoViewModel,
                    navigateToLogin = {
                        appState.navigate(Screen.LoginScreen)
                    },
                    navigateToChatScreen = {
                        appState.navigate(Screen.ChatScreen)
                    },
                    navigateToGenerativeScreen = {
                        appState.navigate(Screen.GenerativeScreen)
                    },
                    navigateBack = appState::navigateUp,
                )
            }

            composable(
                route = Screen.ChatScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300),
                    )
                },
            ) {
                val chatViewModel = koinViewModel<ChatViewModel>()
                ChatScreen(
                    plitsoViewModel = plitsoViewModel,
                    chatViewModel = chatViewModel,
                    navigateBack = appState::navigateUp,
                )
            }

            composable(
                route = Screen.GenerativeScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(300),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(300),
                    )
                },
            ) {
                val generativeViewModel = koinViewModel<GenerativeViewModel>()
                GenerativeScreen(
                    generativeViewModel = generativeViewModel,
                    navigateBack = appState::navigateUp,
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
                    },
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
                    },
                )
            }

            composable(
                route = Screen.LoginScreen.route,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(200),
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(200),
                    )
                },
            ) {
                val loginViewModel = koinViewModel<LoginViewModel>()
                LoginScreen(
                    plitsoViewModel = plitsoViewModel,
                    loginViewModel = loginViewModel,
                    navigateBack = appState::navigateUp,
                )
            }
        }
    }
}
