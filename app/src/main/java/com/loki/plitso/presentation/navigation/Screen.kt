package com.loki.plitso.presentation.navigation

import com.loki.plitso.util.Constants.CATEGORY_ID_KEY
import com.loki.plitso.util.Constants.CATEGORY_NAME_KEY
import com.loki.plitso.util.Constants.RECIPE_ID_KEY

sealed class Screen(
    val route: String,
    var routePath: String? = null,
    val restoreState: Boolean = true,
) {
    data object HomeScreen : Screen("home_screen")

    data object AccountScreen : Screen("account_screen")

    data object SearchScreen : Screen("search_screen")

    data object RecipesScreen : Screen("recipes_screen")

    data object RecipeDetailScreen : Screen("recipe_detail_screen")

    data object DocumentScreen : Screen("document_screen")

    data object FoodDocumentsScreen : Screen("food_documents_screen")

    data object AIScreen : Screen("ai_screen")

    data object ChatScreen : Screen("chat_screen")

    data object GenerativeScreen : Screen("generative_screen")

    data object BookmarkScreen : Screen("bookmark_screen")

    data object LoginScreen : Screen("login_screen")

    fun withCategory(): String =
        "${RecipesScreen.route}/{${CATEGORY_ID_KEY}}/{${CATEGORY_NAME_KEY}}"

    fun withRecipeId(): String = "${RecipeDetailScreen.route}/{${RECIPE_ID_KEY}}"

    fun navWith(path: String) = apply { routePath = path }
}
