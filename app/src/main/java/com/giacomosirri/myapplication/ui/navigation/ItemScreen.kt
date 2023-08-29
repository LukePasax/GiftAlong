package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ItemScreen(paddingValues: PaddingValues, item : String) {
    Text(text ="Item $item", modifier = Modifier.padding(paddingValues))
}