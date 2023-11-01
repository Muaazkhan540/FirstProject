package com.example.firstproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.firstproject.ui.theme.FirstProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    var hasPermission by remember {
                        mutableStateOf(false)
                    }
                    val launcher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
                            onResult = {
                                hasPermission = it
                            })
                    if (hasPermission) SwitchMinimal()
                    else Greeting {
                        if (!hasPermission)
                            launcher.launch(
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else ""
                            )
                    }
                    if (hasPermission()) SwitchMinimal()
                }
            }
        }
    }
}

@Composable
fun Greeting(onclick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { onclick() }) {
            Text(text = "Grant")
        }
    }
}

@Composable
fun SwitchMinimal() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        var checked by remember { mutableStateOf(true) }
        val serviceIntent = Intent(LocalContext.current, NotificationService::class.java)
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
        if (checked) LocalContext.current.startService(serviceIntent)
        else LocalContext.current.stopService(serviceIntent)
    }
}

fun Context.hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        hasPermission(Manifest.permission.POST_NOTIFICATIONS) else true
}

fun Context.hasPermission(vararg permissions: String): Boolean {
    return permissions.all { singlePermission ->
        ContextCompat.checkSelfPermission(
            this,
            singlePermission
        ) == PackageManager.PERMISSION_GRANTED
    }
}