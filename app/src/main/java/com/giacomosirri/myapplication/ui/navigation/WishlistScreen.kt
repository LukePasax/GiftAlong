package com.giacomosirri.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary

@Composable
fun WishlistScreen(username : String,paddingValues: PaddingValues, onFabClick: () -> Unit, navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new item")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { WishlistItem(name = "Ciao", username  = username, navController = navController) }
            item { WishlistItem(name = AppContext.getCurrentUser(), username = username, navController = navController, url = "www.cacca.it") }
        }
    }
}

@Composable
fun WishlistScreen(searchedItems: String) {

}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(name: String, username: String, navController: NavController, url: String? = null, image: Int = R.drawable.placeholder_foreground) {
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable {
                    navController.navigate(NavigationScreen.Item.name + name + "/" + username)
                           },
            headlineContent = { Text(name) },
            supportingContent = { Text(url.orEmpty()) },
            trailingContent = {
                if (username == AppContext.getCurrentUser()) {
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
                } else {
                    Column(horizontalAlignment = Alignment.Start) {
                        Spacer(modifier = Modifier.height(5.dp))
                        CheckboxItem(text = "Reserve it", paddingValues = PaddingValues(end = 0.dp))
                        CheckboxItem(text = "Bought", paddingValues = PaddingValues(end = 0.dp))
                    }
                }

            },
            leadingContent = {
                Image(
                    painterResource(id = image),
                    contentDescription = "Wishlist item image",
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                )
            }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray
        )
    }
}

