package com.loki.plitso.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loki.plitso.presentation.theme.PlitsoTheme
import com.loki.plitso.util.shimmerEffect

@Composable
fun HomeSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(12) {
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(
    showBackground = true, uiMode = 1
)
@Composable
fun HomeSkeletonPreview() {
    PlitsoTheme {
        HomeSkeleton()
    }
}