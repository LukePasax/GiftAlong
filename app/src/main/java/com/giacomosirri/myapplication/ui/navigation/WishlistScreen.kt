package com.giacomosirri.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import com.giacomosirri.myapplication.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.giacomosirri.myapplication.ui.AppContext

@Composable
fun WishlistScreen(
    username: String,
    paddingValues: PaddingValues,
    onFabClick: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName =
                    if (username == AppContext.getCurrentUser()) {
                        "Your Wishlist"
                    } else {
                        "$username's Wishlist"
                    },
                hasSearchBar = true,
                searchBarPlaceholder = "Search an item by its name",
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new item")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { WishlistItem(itemName = "Ciao", username  = username, navController = navController) }
            item { WishlistItem(itemName = AppContext.getCurrentUser(), username = username, navController = navController, url = "www.cacca.it") }
        }
    }
}

@Composable
fun WishlistScreen(searchedItems: String) {

}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(
    itemName: String,
    username: String,
    navController: NavController,
    url: String? = null,
    image: Int = R.drawable.placeholder_foreground
) {
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clickable {
                    navController.navigate(NavigationScreen.Item.name + itemName + "/" + username)
                },
            headlineContent = { Text(itemName) },
            supportingContent = { Text(url.orEmpty()) },
            trailingContent = {
                if (username == AppContext.getCurrentUser()) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(ImageVector.vectorResource(id = R.drawable.baseline_more_horiz_24), "Item Menu")
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(1.dp))
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                        .fillMaxHeight(.85f)
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

