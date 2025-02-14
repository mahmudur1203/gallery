package com.myapp.gallery.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.gallery.util.PERMISSIONS

@SuppressLint("ContextCastToActivity")
@Composable
fun PermissionsScreen(onPermissionGranted: () -> Unit) {

    val activity = LocalContext.current as Activity

    val permissionDialog = remember {
        mutableStateListOf<NeededPermission>()
    }

    val multiplePermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                var allPermissionsGranted = true
                permissions.entries.forEach { entry ->
                    if (!entry.value) {
                        allPermissionsGranted = false
                        permissionDialog.add(getNeededPermission(entry.key))
                    }
                }

                if (allPermissionsGranted) {
                    onPermissionGranted()
                }
            })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Permission Required",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This app needs storage access to display your albums. Please enable the permissions.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    multiplePermissionLauncher.launch(
                        PERMISSIONS.toTypedArray()
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(text = "Grant Permissions", color = Color.White)
            }
        }
    }


    permissionDialog.forEach { permission ->
        PermissionAlertDialog(
            neededPermission = permission,
            onDismiss = { permissionDialog.remove(permission) },
            onOkClick = {
                permissionDialog.remove(permission)
                multiplePermissionLauncher.launch(arrayOf(permission.permission))
            },
            onGoToAppSettingsClick = {
                permissionDialog.remove(permission)
                activity.goToAppSetting()
            },

            isPermissionDeclined = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                !activity.shouldShowRequestPermissionRationale(permission.permission)
            } else {
                true
            }

        )
    }
}


fun Activity.goToAppSetting() {
    val i = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
    )
    startActivity(i)
}

enum class NeededPermission(
    val permission: String,
    val title: String,
    val description: String,
    val permanentlyDeniedDescription: String,
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    READ_MEDIA_IMAGES(
        permission = android.Manifest.permission.READ_MEDIA_IMAGES,
        title = "Read Media Images Permission",
        description = "This permission is needed to read your media images. Please grant the permission.",
        permanentlyDeniedDescription = "This permission is needed to read your media images. Please grant the permission in app settings.",
    ),

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    READ_MEDIA_VIDEO(
        permission = android.Manifest.permission.READ_MEDIA_VIDEO,
        title = "Read Media Video Permission",
        description = "This permission is needed to read your media video. Please grant the permission.",
        permanentlyDeniedDescription = "This permission is needed to read your media video. Please grant the permission in app settings.",
    ),

    READ_EXTERNAL_STORAGE(
        permission = android.Manifest.permission.READ_EXTERNAL_STORAGE,
        title = "Read External Storage Permission",
        description = "This permission is needed to read your external storage. Please grant the permission.",
        permanentlyDeniedDescription = "This permission is needed to read your external storage. Please grant the permission in app settings.",
    );


    fun permissionTextProvider(isPermanentDenied: Boolean): String {
        return if (isPermanentDenied) this.permanentlyDeniedDescription else this.description
    }
}

fun getNeededPermission(permission: String): NeededPermission {
    return NeededPermission.entries.find { it.permission == permission }
        ?: throw IllegalArgumentException("Permission $permission is not supported")
}


@Composable
fun PermissionAlertDialog(
    neededPermission: NeededPermission,
    isPermissionDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(text = if (isPermissionDeclined) "Go to app setting" else "OK",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermissionDeclined) onGoToAppSettingsClick()
                            else onOkClick()

                        }
                        .padding(16.dp))
            }
        },


        title = {
            Text(
                text = neededPermission.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = neededPermission.permissionTextProvider(isPermissionDeclined),
            )
        },
    )
}
