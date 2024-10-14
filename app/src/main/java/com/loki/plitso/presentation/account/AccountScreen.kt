package com.loki.plitso.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loki.plitso.PlitsoViewModel
import com.loki.plitso.data.local.datastore.Theme
import com.loki.plitso.util.noIndication
import com.loki.plitso.util.showToast
import java.util.Locale

@Composable
fun AccountScreen(
    plitsoViewModel: PlitsoViewModel,
    accountState: AccountState,
    onThemeChange: (theme: String) -> Unit,
    onLogOut: () -> Unit,
    navigateToSignIn: () -> Unit,
) {

    var isThemeDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user by plitsoViewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(accountState.message) {
        if (accountState.message.isNotBlank()) {
            context.showToast(accountState.message)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (isThemeDialogVisible) {
            ThemeDialog(
                themeSelected = accountState.theme,
                onThemeChange = onThemeChange,
                onDismiss = {
                    isThemeDialogVisible = false
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Account",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            if (!user.isLoggedIn) {
                LoginContainer(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    onLoginClick = navigateToSignIn
                )
            } else {
                TitleHeader(title = "Profile")

                SettingItem(
                    itemList = listOf(
                        SettingItem(
                            icon = Icons.Default.AccountCircle,
                            content = user.username,
                            imageUrl = user.imageUrl,
                            subContent = {
                                TextSubContent(content = user.email)
                            }
                        )
                    )
                )
            }

            TitleHeader(title = "Settings")

            SettingItem(
                itemList = listOf(
                    SettingItem(
                        icon = Icons.Default.Palette,
                        content = "Select Theme",
                        subContent = {
                            TextSubContent(content = accountState.theme)
                        }
                    )
                ),
                onItemClick = { itemIndex ->
                    isThemeDialogVisible = true
                }
            )

            if (user.isLoggedIn) {
                SettingItem(
                    modifier = Modifier.padding(top = 12.dp),
                    itemList = listOf(
                        SettingItem(
                            icon = Icons.AutoMirrored.Filled.Logout,
                            content = "Logout"
                        )
                    ),
                    onItemClick = {
                        onLogOut()
                    }
                )
            }
        }
    }
}

@Composable
private fun TextSubContent(modifier: Modifier = Modifier, content: String) {
    Text(
        text = content,
        color = MaterialTheme.colorScheme.onBackground.copy(.5f),
        modifier = modifier
    )
}

@Composable
fun ThemeDialog(
    modifier: Modifier = Modifier,
    themeSelected: String,
    onThemeChange: (theme: String) -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Box(
            modifier = modifier
                .width(300.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {

            Column(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Select Theme",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Theme.entries.forEach { theme ->

                    val selected = themeSelected == theme.name

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noIndication {
                                onThemeChange(theme.name)
                            }
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = theme.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            RadioButton(
                                selected = selected,
                                onClick = {
                                    onThemeChange(theme.name)
                                }
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }

                    TextButton(onClick = onDismiss) {
                        Text(text = "Ok")
                    }
                }
            }
        }

    }
}

@Composable
fun TitleHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}

@Composable
fun LoginContainer(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = .5.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface.copy(.5f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Login to your account",
                color = MaterialTheme.colorScheme.primary.copy(.8f)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onLoginClick) {
                Text(text = "Login")
            }
        }
    }
}