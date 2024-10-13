package com.loki.plitso.presentation.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.loki.plitso.data.local.models.RecipeDetail
import com.loki.plitso.presentation.document.components.textFieldColors
import com.loki.plitso.util.noIndication

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    searchViewModel: SearchViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToRecipeDetailScreen: (id: String) -> Unit
) {

    val recipes by searchViewModel.recipes.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(.2f),
                    shape = CircleShape
                )
        ) {

            val focusRequester = remember {
                FocusRequester()
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = searchViewModel.searchTerm.value,
                onValueChange = searchViewModel::onSearchTermChange,
                placeholder = {
                    Text(
                        text = "Search eg. ingredients, food",
                        color = MaterialTheme.colorScheme.onBackground.copy(.2f)
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "search icon"
                    )
                },
                colors = textFieldColors()
            )
        }

        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No search",
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(recipes) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onItemClick = navigateToRecipeDetailScreen
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeItem(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipe: RecipeDetail,
    onItemClick: (id: String) -> Unit
) {
    Column(
        modifier = Modifier.noIndication {
            onItemClick(recipe.recipeId)
        }
    ) {
        Box(
            modifier = modifier
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            SubcomposeAsyncImage(
                model = recipe.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/${recipe.recipeId}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 200)
                        }
                    ),
                loading = {
                    Icon(
                        imageVector = Icons.Filled.Fastfood,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(.2f)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = recipe.title,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(
                        key = "text/${recipe.recipeId}"
                    ),
                    animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 200)
                    }
                )
        )
    }
}