package com.loki.plitso.presentation.permission

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationPermissionRequest(
    context: Context,
    onGrantedPermission: (isGranted: Boolean) -> Unit
) {
    val isGranted = checkIfPermissionGranted(context, Manifest.permission.POST_NOTIFICATIONS)

    if (isGranted){
        onGrantedPermission(true)
    } else {

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            onGrantedPermission(result)
        }

        SideEffect {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED)
}