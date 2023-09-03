package com.giacomosirri.myapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.GiftAlong
import com.giacomosirri.myapplication.ui.navigation.NavigationApp
import com.giacomosirri.myapplication.ui.theme.MyApplicationTheme
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.AppViewModelFactory
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModelFactory
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
        val appViewModel by viewModels<AppViewModel> {
            val app = application as GiftAlong
            AppViewModelFactory(app.eventRepository, app.itemRepository, app.userRepository)
        }
        val settingsViewModel by viewModels<SettingsViewModel> {
            val app = application as GiftAlong
            SettingsViewModelFactory(app.settingsRepository)
        }
        setContent {
            AppContext.setContext(applicationContext)
            val isAutoAuthActive by settingsViewModel.isAutoAuthActive.collectAsState(initial = false)
            val authUser by settingsViewModel.authenticatedUser.collectAsState(initial = "")
            if (isAutoAuthActive) {
                AppContext.setCurrentUser(authUser)
            }
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationApp(
                        paddingValues = PaddingValues(top = 70.dp),
                        appViewModel = appViewModel,
                        settingsViewModel = settingsViewModel,
                        isUserLoggedIn = isAutoAuthActive
                    )
                }
            }
        }
    }
}