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
import androidx.compose.runtime.*
import com.giacomosirri.myapplication.R
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
import androidx.navigation.NavController
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Secondary
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList

@Composable
fun WishlistScreen(
    username: String,
    paddingValues: PaddingValues,
    onFabClick: () -> Unit,
    navController: NavController,
    viewModel: AppViewModel
) {
    val items = viewModel.getItemsOfUser(username).collectAsState(initial = emptyList())
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
            for (item in items.value) {
                item {
                    WishlistItem(
                        itemId = item.id,
                        itemName = item.name,
                        username = username,
                        priceL = item.priceLowerBound,
                        priceU = item.priceUpperBound,
                        reservingUser = item.reservedBy,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistScreen(searchedItems: String) {

}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(
    itemId: Int,
    itemName: String,
    username: String,
    priceL: Double? = null,
    priceU: Double? = null,
    image: Int = R.drawable.placeholder,
    reservingUser: String? = null,
    navController: NavController,
    viewModel: AppViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    val reserved = remember { mutableStateOf(false) }
    val bought = remember { mutableStateOf(false) }
    val isMenuOpen = remember { mutableStateOf(false) }
    val isCancelItemDialogOpen = remember { mutableStateOf(false) }
    if (isCancelItemDialogOpen.value) {
        DefinitiveDeletionDialog(
            isDialogOpen = isCancelItemDialogOpen,
            onAccept = {
                       viewModel.deleteItem(itemId)
            },
            dialogTitle = "Item deletion",
            mainText = "Are you sure you want to delete this item from your wishlist? This operation cannot be undone.",
            acceptText = "Yes",
            refuseText = "Don't delete"
        )
    }
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
                    Box {
                        IconButton(onClick = { isMenuOpen.value = true }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_more_horiz_24),
                                contentDescription = "Item Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuOpen.value, 
                            onDismissRequest = { isMenuOpen.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    isMenuOpen.value = false
                                    navController.navigate(NavigationScreen.NewItem.name + itemId)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = { isCancelItemDialogOpen.value = true }
                            )
                        }
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
                        if (priceL != null && priceU != null) {
                            Text(
                                text = "€$priceL - €$priceU",
                                fontSize = 17.sp)
                        } else {
                            Text(
                                text = "No price",
                                fontSize = 17.sp)
                        }
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
        ItemDialog(navController, itemName, openDialog, username, reserved, bought, reservingUser)
    }
}

@Composable
fun ItemDialog(
    navController: NavController,
    itemName: String,
    openDialog: MutableState<Boolean>,
    username: String,
    // The user that has ALREADY reserved this item, if it exists.
    reserved: MutableState<Boolean>,
    bought: MutableState<Boolean>,
    reservingUser: String? = null,
) {
    val description = "This is a description"
    val url = "www.url.it"
    val buyButtonText = if (reserved.value && bought.value) "Bought" else "Buy"
    val reserveButtonText = if (reserved.value) "Unreserve" else "Reserve"
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
                    if (reservingUser != null) {
                        DialogEntry(
                            paddingValues = PaddingValues(horizontal = 15.dp),
                            text = "Reserved by:",
                            value = {
                                OutlinedButton(
                                    onClick = {
                                        openDialog.value = false
                                        navController.navigate(NavigationScreen.UserProfile.name + reservingUser)
                                    }
                                ) {
                                    Text(text = reservingUser)
                                }
                            }
                        )
                    }
                    /* TODO consider changing how these buttons work because it's not great UX now */
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        composable1 = {
                            Button(
                                onClick = { reserved.value = !reserved.value },
                                enabled = !bought.value,
                                modifier = Modifier.size(120.dp, 45.dp)
                            ) {
                                Text(text = reserveButtonText)
                            }
                        },
                        composable2 = {
                            Button(
                                onClick = { bought.value = !bought.value },
                                enabled = reserved.value,
                                modifier = Modifier.size(120.dp, 45.dp)
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

