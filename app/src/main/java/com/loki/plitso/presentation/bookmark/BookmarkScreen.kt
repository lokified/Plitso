package com.loki.plitso.presentation.bookmark

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.loki.plitso.data.local.models.Bookmark
import com.loki.plitso.util.noIndication

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BookmarkScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    bookmarks: List<Bookmark>,
    navigateToRecipeDetailScreen: (id: String) -> Unit,
) {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Bookmark",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Nothing to see here",
                    color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(bookmarks) { bookmark ->
                    BookmarkItem(
                        animatedVisibilityScope = animatedVisibilityScope,
                        bookmark = bookmark,
                        onItemClick = { navigateToRecipeDetailScreen(bookmark.recipeId) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.BookmarkItem(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    bookmark: Bookmark,
    onItemClick: (id: String) -> Unit,
) {
    Column(
        modifier =
            Modifier.noIndication {
                onItemClick(bookmark.recipeId)
            },
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
        ) {
            SubcomposeAsyncImage(
                model = bookmark.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .sharedElement(
                            state = rememberSharedContentState(key = "image/${bookmark.recipeId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 200)
                            },
                        ),
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
            text = bookmark.title,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier =
                Modifier
                    .sharedElement(
                        rememberSharedContentState(
                            key = "text/${bookmark.recipeId}",
                        ),
                        animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 200)
                        },
                    ),
        )
    }
}
