package com.loki.plitso.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.loki.plitso.presentation.navigation.Screen

@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    navController: NavController,
    onItemClick: (Screen) -> Unit
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val bottomBarDestination = bottomNavItems.any { it.screen.route == currentDestination?.route }

    if (bottomBarDestination && currentDestination?.route != Screen.DocumentScreen.route) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                bottomNavItems.forEach { bottomNavItem ->
                    val selected = bottomNavItem.screen.route == currentDestination?.route

                    val interactionSource = remember { MutableInteractionSource() }

                    val iconColor by animateColorAsState(
                        targetValue = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground.copy(.5f),
                        label = "unselectedIcon color",
                        animationSpec = tween(
                            easing = EaseInOut
                        )
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f / bottomNavItems.size),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    onItemClick(bottomNavItem.screen)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (selected) bottomNavItem.selectedIcon else bottomNavItem.unselectedIcon,
                                contentDescription = bottomNavItem.title,
                                tint = iconColor
                            )
                        }
                    }
                }
            }
        }
    }
}

val bottomNavItems = listOf(
    BottomNavItem(
        "Home",
        Icons.Outlined.Home,
        Icons.Filled.Home,
        Screen.HomeScreen
    ),
    BottomNavItem(
        "Document",
        Icons.Outlined.Book,
        Icons.Filled.Book,
        Screen.DocumentScreen
    ),
    BottomNavItem(
        "Bookmark",
        Icons.Outlined.BookmarkBorder,
        Icons.Filled.Bookmark,
        Screen.BookmarkScreen
    ),
    BottomNavItem(
        "Account",
        Icons.Outlined.AccountCircle,
        Icons.Filled.AccountCircle,
        Screen.AccountScreen
    )
)

data class BottomNavItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val screen: Screen
)