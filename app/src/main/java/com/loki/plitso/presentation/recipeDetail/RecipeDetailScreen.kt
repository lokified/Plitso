package com.loki.plitso.presentation.recipeDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.loki.plitso.util.noIndication

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeDetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipeDetailState: RecipeDetailState,
    navigateBack: () -> Unit,
    onAddToBookmark: () -> Unit,
) {
    if (recipeDetailState.recipeDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Data loading. Please wait ...",
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            )
        }
    } else {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            TopSection(
                animatedVisibilityScope = animatedVisibilityScope,
                recipeDetailState = recipeDetailState,
                navigateBack = navigateBack,
                onAddToBookmark = onAddToBookmark,
            )
            ContentSection(
                animatedVisibilityScope = animatedVisibilityScope,
                recipeDetailState = recipeDetailState,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.TopSection(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipeDetailState: RecipeDetailState,
    navigateBack: () -> Unit,
    onAddToBookmark: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(350.dp),
    ) {
        recipeDetailState.recipeDetail?.let { recipe ->
            SubcomposeAsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
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
                        .height(100.dp)
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

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recipe.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = Color.White,
                    modifier =
                        Modifier
                            .width(250.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onAddToBookmark) {
                    val icon =
                        if (recipeDetailState.isBookmarked) {
                            Icons.Filled.Bookmark
                        } else {
                            Icons.Outlined.BookmarkBorder
                        }
                    val color =
                        if (recipeDetailState.isBookmarked) {
                            Color.Yellow.copy(.7f)
                        } else {
                            Color.White
                        }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ContentSection(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipeDetailState: RecipeDetailState,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Column {
            recipeDetailState.recipeDetail?.let { recipe ->
                Text(
                    text = recipe.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier =
                        Modifier
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

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier =
                        Modifier
                            .padding(4.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(.5f),
                                shape = RoundedCornerShape(12.dp),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = recipe.country,
                        color = MaterialTheme.colorScheme.primary.copy(.5f),
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                ) {
                    Column {
                        var isIngredientOpen by remember { mutableStateOf(false) }

                        RowActionComponent(
                            title = "Ingredients",
                            isExpanded = isIngredientOpen,
                            onExpandedClick = { isIngredientOpen = !isIngredientOpen },
                        )

                        AnimatedVisibility(
                            visible = isIngredientOpen,
                            exit =
                                fadeOut() +
                                    shrinkVertically(
                                        tween(easing = EaseOut),
                                    ),
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Column {
                                recipe.ingredients.forEach { ingredient ->
                                    Text(
                                        text = ingredient,
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onBackground.copy(.1f),
                        )

                        var isInstructionOpen by remember { mutableStateOf(true) }
                        val instList =
                            recipe.instructions!!.split("\r\n").filter { it.isNotEmpty() }

                        RowActionComponent(
                            title = "Instructions",
                            isExpanded = isInstructionOpen,
                            onExpandedClick = { isInstructionOpen = !isInstructionOpen },
                        )

                        AnimatedVisibility(
                            visible = isInstructionOpen,
                            exit =
                                fadeOut() +
                                    shrinkVertically(
                                        tween(easing = EaseOut),
                                    ),
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Column {
                                instList.forEach { inst ->
                                    Text(
                                        text = inst,
                                        color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowActionComponent(
    modifier: Modifier = Modifier,
    title: String = "",
    isExpanded: Boolean,
    onExpandedClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .padding(12.dp)
                .fillMaxWidth()
                .noIndication {
                    onExpandedClick()
                },
    ) {
        val icon =
            if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onExpandedClick() }) {
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}
