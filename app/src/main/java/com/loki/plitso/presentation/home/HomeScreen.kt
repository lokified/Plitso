package com.loki.plitso.presentation.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.data.local.datastore.LocalUser
import com.loki.plitso.data.local.models.Category
import com.loki.plitso.data.local.models.DayRecipe
import com.loki.plitso.presentation.components.HomeSkeleton
import com.loki.plitso.util.noIndication

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    homeViewModel: HomeViewModel,
    plitsoViewModel: PlitsoViewModel,
    navigateToAccountScreen: () -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToRecipesScreen: (Category) -> Unit,
    navigateToRecipeDetailScreen: (id: String) -> Unit,
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val dayRecipe by homeViewModel.dayRecipe.collectAsStateWithLifecycle()
    val user by plitsoViewModel.user.collectAsStateWithLifecycle()

    if (dayRecipe == null) {
        HomeSkeleton()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            gridColItem {
                HomeTopBar(
                    user = user,
                    navigateToAccountScreen = navigateToAccountScreen,
                    navigateToSearchScreen = navigateToSearchScreen,
                )
            }

            gridColItem {
                RecipeOfDayComponent(
                    animatedVisibilityScope = animatedVisibilityScope,
                    dayRecipe = dayRecipe,
                    navigateToRecipeDetailScreen = navigateToRecipeDetailScreen,
                )
            }

            gridColItem {
                RandomRecipe(
                    homeState = homeState,
                    navigateToRecipeDetailScreen = navigateToRecipeDetailScreen,
                )
            }

            items(homeState.categories) { category ->
                CategoryItem(
                    category = category,
                    onItemClick = {
                        navigateToRecipesScreen(category)
                    },
                )
            }
        }
    }
}

private fun LazyGridScope.gridColItem(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(this.maxLineSpan) }) {
        content()
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    user: LocalUser,
    navigateToAccountScreen: () -> Unit,
    navigateToSearchScreen: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (user.isLoggedIn) {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = user.username,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.noIndication { navigateToAccountScreen() },
            )
            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = navigateToAccountScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.scale(.6f),
                )
            }
        } else {
            Text(
                text = "Welcome",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = navigateToSearchScreen) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.RecipeOfDayComponent(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    dayRecipe: DayRecipe?,
    navigateToRecipeDetailScreen: (id: String) -> Unit,
) {
    dayRecipe?.let { recipe ->
        BoxWithConstraints(
            modifier =
                modifier
                    .clip(RoundedCornerShape(16.dp))
                    .height(370.dp)
                    .noIndication { navigateToRecipeDetailScreen(recipe.recipeId) },
        ) {
            val width = this.maxWidth

            AsyncImage(
                model = recipe.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .sharedElement(
                            state = rememberSharedContentState(key = "image/${recipe.recipeId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 200)
                            },
                        ),
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .height(200.dp)
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(.7f),
                                        Color.Black.copy(.4f),
                                        Color.Transparent,
                                    ),
                                ),
                        ),
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.TopCenter),
            ) {
                Text(
                    text = "RECIPE OF THE DAY",
                    fontSize = 18.sp,
                    color = Color.White.copy(.5f),
                    modifier =
                        Modifier
                            .padding(top = 8.dp)
                            .padding(vertical = 8.dp),
                )

                Text(
                    text = recipe.title,
                    fontSize = 28.sp,
                    lineHeight = 30.sp,
                    color = Color.White,
                    modifier =
                        Modifier
                            .width(width * 3 / 4)
                            .sharedElement(
                                rememberSharedContentState(
                                    key = "text/${recipe.recipeId}",
                                ),
                                animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 200)
                                },
                            ),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color.Yellow.copy(.7f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RandomRecipe(
    modifier: Modifier = Modifier,
    homeState: HomeState,
    navigateToRecipeDetailScreen: (id: String) -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .noIndication { navigateToRecipeDetailScreen(homeState.randomRecipeId) },
    ) {
        SubcomposeAsyncImage(
            model = homeState.randomRecipeImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .size(width = 100.dp, height = 80.dp),
            loading = {
                Icon(
                    imageVector = Icons.Filled.Fastfood,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                )
            },
        )

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.width(100.dp),
            )

            Spacer(modifier = Modifier.weight(1f))

            Column {
                Text(text = "Random recipe", fontSize = 20.sp)
                Text(
                    text = "Don't know what to cook?",
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(.5f))
                .noIndication { onItemClick() },
    ) {
        Box(
            modifier = Modifier,
        ) {
            SubcomposeAsyncImage(
                model = category.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                loading = {
                    Icon(
                        imageVector = Icons.Filled.Fastfood,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(.2f),
                    )
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
