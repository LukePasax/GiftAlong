package com.giacomosirri.myapplication.ui.navigation

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import com.giacomosirri.myapplication.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary

@Composable
fun WishlistScreen(paddingValues: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        item { WishlistItem(name = "Ciao") }
        item { WishlistItem(name = "Caccone", url = "www.cacca.it")}
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(name: String, url: String? = null, image: ImageBitmap? = null) {
    val icon = R.drawable.placeholder_foreground
    if (image != null) {
        //TODO: set icon to image
    }
    ListItem(
        headlineContent = { Text(name) },
        supportingContent = { Text(url.orEmpty()) },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Row {
                    Checkbox(checked = false, onCheckedChange = null)
                    Text(text = "Mark as received")
                }
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
}