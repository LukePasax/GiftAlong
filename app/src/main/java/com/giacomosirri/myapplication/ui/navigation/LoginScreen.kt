package com.giacomosirri.myapplication.ui.navigation

import com.giacomosirri.myapplication.ui.theme.Typography
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    Column {
        Text(text = "GiftAlong", fontSize = 30.sp, style = Typography.headlineLarge)
        OutlinedTextField(value = "Username", onValueChange = "Username"::plus, label = { Text("Username") })
        OutlinedTextField(value = "Password", onValueChange = "Password"::plus, label = { Text("Password") })

    }
}