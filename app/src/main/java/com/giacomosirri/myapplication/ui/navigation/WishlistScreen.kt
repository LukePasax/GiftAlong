package com.giacomosirri.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import com.giacomosirri.myapplication.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary

@Composable
fun WishlistScreen(paddingValues: PaddingValues, onFabClick: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new item")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { WishlistItem(name = "Ciao") }
            item { WishlistItem(name = AppContext.getCurrentUser(), url = "www.cacca.it") }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(name: String, url: String? = null, image: ImageBitmap? = null) {
    var checked by mutableStateOf(false)
    val icon = R.drawable.placeholder_foreground
    if (image != null) {
        //TODO: set icon to image
    }
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            headlineContent = { Text(name) },
            supportingContent = { Text(url.orEmpty()) },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    Spacer(modifier = Modifier.height(5.dp))
                    CheckboxItem(text = "Received", paddingValues = PaddingValues(end = 0.dp))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Primary
                        )
                    }
                }
            },
            leadingContent = {
                Image(
                    painterResource(id = icon),
                    contentDescription = "Wishlist item image",
                )
            }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray
        )
    }
}