package com.loki.plitso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loki.plitso.presentation.components.BottomNav
import com.loki.plitso.presentation.components.PermissionDialog
import com.loki.plitso.presentation.navigation.AppState
import com.loki.plitso.presentation.navigation.Navigation
import com.loki.plitso.presentation.navigation.Screen
import com.loki.plitso.presentation.permission.NotificationPermissionRequest
import com.loki.plitso.presentation.theme.PlitsoTheme
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.showToast
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val plitsoViewModel: PlitsoViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val theme by plitsoViewModel.theme.collectAsStateWithLifecycle()
            PlitsoTheme(
                theme = theme
            ) {

                val context = LocalContext.current
                var showNotificationDialog by remember { mutableStateOf(false) }

                val settingsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) {  result ->
                    if (result.resultCode == RESULT_OK) {
                        showNotificationDialog = false
                        context.showToast("Permission Granted")
                    } else {
                        context.showToast("Permission Denied")
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    NotificationPermissionRequest(
                        context = context,
                        onGrantedPermission = { isGranted ->
                            showNotificationDialog = !isGranted
                        }
                    )
                }

                if (showNotificationDialog) {
                    PermissionDialog(
                        title = R.string.notification_permission_request,
                        content = R.string.permission_is_required_to_show_app_notifications,
                        onDismiss = {},
                        onRequest = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            settingsLauncher.launch(intent)
                        }
                    )
                }

                MainApp(plitsoViewModel = plitsoViewModel)
            }
        }
    }
}

@Composable
fun MainApp(
    plitsoViewModel: PlitsoViewModel
) {
    val appState = rememberAppState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNav(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                navController = appState.navController,
                onItemClick = { screen ->
                    appState.bottomNavNavigate(screen.route)
                }
            )
        },
        floatingActionButton = {
            val backStackEntry by appState.navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val routes =
                listOf(
                    Screen.RecipesScreen.route,
                    Screen.HomeScreen.route
                )
            if (routes.any { it == currentDestination?.route }) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .noIndication {
                            appState.navigate(Screen.AIScreen)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Navigation(
                appState = appState,
                plitsoViewModel = plitsoViewModel
            )
        }
    }
}

@Composable
fun rememberAppState(
    navHostController: NavHostController = rememberNavController()
) = remember(navHostController) {
    AppState(navHostController)
}