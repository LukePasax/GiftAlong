package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.giacomosirri.myapplication.ui.theme.Primary

@Composable
fun WishlistScreen(paddingValues: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        item { WishlistItem(name = "Ciao") }
        item { WishlistItem(name = "Caccone", url = "www.cacca.it")}
    }
}

@Composable
fun WishlistItem(name: String, url: String? = null, image: ImageBitmap? = null) {
    if (image == null) {
       ListItem(
           headlineContent = { Text(name) },
           supportingContent = { Text(url.orEmpty()) },
           trailingContent = {
               IconButton(onClick = { /*TODO*/ }) {
                   Icon(imageVector = Icons.Filled.Delete, contentDescription = "Menu", tint = Primary)
               }
           }
       )
    } else {
        ListItem(
            headlineContent = { Text(name) },
            supportingContent = { Text(url.orEmpty()) },
            leadingContent = { Image(bitmap = image, contentDescription = "Image") }
        )
    }
}