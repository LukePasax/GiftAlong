package com.giacomosirri.myapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.navigation.NavigationApp
import com.giacomosirri.myapplication.ui.theme.MyApplicationTheme
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import java.lang.ref.WeakReference

class AppContext private constructor() {
    companion object {
        private lateinit var context: WeakReference<Context>
        private lateinit var username: String

        fun setContext(context: Context) {
            this.context = WeakReference(context.applicationContext)
        }

        fun getContext(): Context? = context.get()

        fun setCurrentUser(username: String) {
            this.username = username
        }

        fun getCurrentUser() = this.username
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appViewModel: AppViewModel by viewModels()
        val settingsViewModel: SettingsViewModel by viewModels()
        AppContext.setContext(applicationContext)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationApp(
                        paddingValues = PaddingValues(top = 70.dp),
                        appViewModel = appViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}