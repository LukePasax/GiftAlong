package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R

@Composable
fun RelationshipsScreen(paddingValues: PaddingValues) {
    Scaffold(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { RelationshipListItem(username = "chiaaara", image = null, type = "Friend") }
            item { RelationshipListItem(username = "lukepasax", image = null, type = "Friend") }
            item { RelationshipListItem(username = "erzava", image = null, type = "Friend") }
        }
    }
}

@Composable
fun RelationshipListItem(username: String, image: ImageBitmap?, type: String) {
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
            headlineContent = { Text(username) },
            trailingContent = {
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
