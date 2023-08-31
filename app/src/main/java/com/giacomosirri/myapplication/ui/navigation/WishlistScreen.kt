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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Secondary

@Composable
fun WishlistScreen(
    username: String,
    paddingValues: PaddingValues,
    onFabClick: () -> Unit
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
            item { WishlistItem(itemName = "Ciao", username  = username) }
            item { WishlistItem(itemName = AppContext.getCurrentUser(), username = username) }
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
    price: String?= null,
    image: Int = R.drawable.placeholder
) {
    val openDialog = remember { mutableStateOf(false) }
    val reserved = remember { mutableStateOf(false) }
    val bought = remember { mutableStateOf(false) }
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clickable {
                    openDialog.value = true
                },
            headlineContent = {
                Text(
                    text = itemName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            trailingContent = {
                if (username == AppContext.getCurrentUser()) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(ImageVector.vectorResource(id = R.drawable.baseline_more_horiz_24), "Item Menu")
                    }
                } else {
                    if (reserved.value && !bought.value) {
                        Text(
                            text = "Reserved",
                            fontSize = 17.sp,
                            modifier = Modifier
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                                .padding(10.dp))
                    } else if (bought.value) {
                        Text(
                            text = "Bought",
                            fontSize = 17.sp,
                            modifier = Modifier
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                                .padding(10.dp))
                    } else {
                        Text(text = price ?: "No price", fontSize = 17.sp)
                    }
                }
            },
            leadingContent = {
                Image(
                    painterResource(id = image),
                    contentDescription = "Wishlist item image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight(.85f)
                        .fillMaxWidth(.2f)
                        .clip(RoundedCornerShape(5.dp))
                )
            }
        )
        ListItemDivider()
    }
    if (openDialog.value) {
        ItemDialog(itemName, openDialog, username, reserved, bought)
    }
}

@Composable
fun ItemDialog(itemName: String, openDialog: MutableState<Boolean>, username: String, reserved: MutableState<Boolean>, bought: MutableState<Boolean>) {
    val description = "This is a description"
    val reservingUser = "Sergio"
    val url = "www.url.it"
    val buyButtonText = if (reserved.value && bought.value) "Bought" else "Buy"
    val reserveButtonText = if (reserved.value) "Reserved" else "Reserve"
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        DialogCard(
            minHeight = 300.dp,
            maxHeight = 600.dp,
            elevations = 10.dp,
            colors = CardDefaults.cardColors(containerColor = Secondary)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                val entryPaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                DialogImage(imageDescription = "Item Image", imageId = R.drawable.placeholder)
                DialogTitle(paddingValues = entryPaddingValues, text = itemName)
                DialogEntry(paddingValues = entryPaddingValues, text = "Link: ", value = url)
                DialogEntry(
                    paddingValues = entryPaddingValues,
                    text = "Price Range: ",
                    value = "10-20€"
                )
                DialogEntry(
                    paddingValues = entryPaddingValues,
                    text = "Description:",
                    value = description
                )
                if (username != AppContext.getCurrentUser()) {
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        text = "Reserved by: ",
                        value = {
                            TextButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Text(text = reservingUser)
                            }
                        }
                    )
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        composable1 = {
                            Button(
                                onClick = { reserved.value = !reserved.value },
                                enabled = !bought.value,
                                modifier = Modifier.size(110.dp, 45.dp)
                            ) {
                                Text(text = reserveButtonText)
                            }
                        },
                        composable2 = {
                            Button(
                                onClick = { bought.value = !bought.value },
                                enabled = reserved.value,
                                modifier = Modifier.size(110.dp, 45.dp)
                            ) {
                                Text(text = buyButtonText)
                            }
                        }
                    )
                }
            }
        }
    }
}

