package com.giacomosirri.myapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.navigation.NavigationApp
import com.giacomosirri.myapplication.ui.theme.MyApplicationTheme
import java.lang.ref.WeakReference

class AppContext private constructor() {
    companion object {
        private lateinit var context: WeakReference<Context>

        fun setContext(context: Context) {
            this.context = WeakReference(context.applicationContext)
        }

        fun getContext(): Context? {
            return context.get()
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContext.setContext(applicationContext)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationApp(paddingValues = PaddingValues(top = 70.dp))
                }
            }
        }
    }
}