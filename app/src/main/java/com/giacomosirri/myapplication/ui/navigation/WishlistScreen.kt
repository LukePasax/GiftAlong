package com.giacomosirri.myapplication.ui.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.giacomosirri.myapplication.data.entity.Item
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun WishlistScreen(
    username: String,
    paddingValues: PaddingValues,
    onFabClick: () -> Unit,
    navController: NavController,
    viewModel: AppViewModel,
    query: String? = null
) {
    val items = viewModel.getItemsOfUser(username).collectAsState(initial = emptyList())
    if (query == null) {
        Scaffold(
            topBar = {
                NavigationAppBar(
                    currentScreenName =
                    if (username == AppContext.getCurrentUser()) {
                        AppContext.getContext()!!.getString(R.string.title_user_wishlist)
                    } else {
                        AppContext.getContext()!!.getString(R.string.title_other_user_wishlist, username)
                    },
                    hasSearchBar = true,
                    onSearch = {
                        navController.popBackStack()
                        navController.navigate("${NavigationScreen.Wishlist.name}?query=\"\"")
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onFabClick) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = AppContext.getContext()!!.getString(R.string.description_fab_wishlist_screen))
                }
            }
        ) {
            ItemsList(username, paddingValues, items.value, navController, viewModel)
        }
    } else {
        val itemsToShow = items.value.filter { it.name.startsWith(query) }
        SearchBar(
            searchBarPlaceholder = AppContext.getContext()!!.getString(R.string.search_hint_item),
            currentScreen = NavigationScreen.Wishlist.name,
            onGoBack = {
                navController.popBackStack()
                navController.navigate(NavigationScreen.Wishlist.name + AppContext.getCurrentUser())
            },
            onClear = {
                navController.popBackStack()
                navController.navigate("${NavigationScreen.Wishlist.name}${AppContext.getCurrentUser()}?query=\"\"" )
            }
        ) {
            ItemsList(username, PaddingValues(0.dp), itemsToShow, navController, viewModel)
        }
    }
}

@Composable
fun ItemsList(
    username: String,
    paddingValues: PaddingValues,
    items: List<Item>,
    navController: NavController,
    viewModel: AppViewModel
) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        for (item in items) {
            item {
                WishlistItem(
                    itemId = item.id!!,
                    itemName = item.name,
                    username = username,
                    image = item.imageUri,
                    priceL = item.priceLowerBound,
                    priceU = item.priceUpperBound,
                    reservingUser = item.reservedBy,
                    bought = item.bought,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WishlistItem(
    itemId: Int,
    itemName: String,
    username: String,
    priceL: Int? = null,
    priceU: Int? = null,
    image: String? = null,
    reservingUser: String? = null,
    bought: Boolean = false,
    navController: NavController,
    viewModel: AppViewModel
) {
    val openDialog = remember { mutableStateOf(false) }
    val reserved = remember { mutableStateOf(!reservingUser.isNullOrEmpty()) }
    val isBought = remember { mutableStateOf(bought) }
    val isMenuOpen = remember { mutableStateOf(false) }
    val isCancelItemDialogOpen = remember { mutableStateOf(false) }
    if (isCancelItemDialogOpen.value) {
        DefinitiveDeletionDialog(
            isDialogOpen = isCancelItemDialogOpen,
            onAccept = { viewModel.deleteItem(itemId) },
            dialogTitle = AppContext.getContext()!!.getString(R.string.dialog_item_delete_title),
            mainText = AppContext.getContext()!!.getString(R.string.dialog_item_delete),
            acceptText = AppContext.getContext()!!.getString(R.string.btn_yes),
            refuseText = AppContext.getContext()!!.getString(R.string.btn_dont_delete)
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
                                contentDescription = AppContext.getContext()!!.getString(R.string.description_item_menu_icon)
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuOpen.value, 
                            onDismissRequest = { isMenuOpen.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(AppContext.getContext()!!.getString(R.string.dropdown_item_edit)) },
                                onClick = {
                                    isMenuOpen.value = false
                                    navController.navigate(NavigationScreen.NewItem.name + itemId)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(AppContext.getContext()!!.getString(R.string.dropdown_item_delete)) },
                                onClick = { isCancelItemDialogOpen.value = true }
                            )
                        }
                    }
                } else {
                    if (reserved.value && !isBought.value) {
                        Text(
                            text = AppContext.getContext()!!.getString(R.string.item_status_reserved),
                            fontSize = 17.sp,
                            modifier = Modifier
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                                .padding(10.dp))
                    } else if (isBought.value) {
                        Text(
                            text = AppContext.getContext()!!.getString(R.string.item_status_bought),
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
                                text = AppContext.getContext()!!.getString(R.string.item_no_price),
                                fontSize = 17.sp)
                        }
                    }
                }
            },
            leadingContent = {
                if (image != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(AppContext.getContext()!!).data(Uri.parse(image)).crossfade(true).build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight(.85f)
                            .fillMaxWidth(.2f)
                            .clip(RoundedCornerShape(5.dp))
                    )
                } else {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight(.85f)
                            .fillMaxWidth(.2f)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        )
        ListItemDivider()
    }
    if (openDialog.value) {
        ItemDialog(navController, itemId, itemName, image, username, reservingUser, reserved, isBought, openDialog, viewModel)
    }
}

@Composable
fun ItemDialog(
    navController: NavController,
    itemId: Int,
    itemName: String,
    image: String?,
    username: String,
    reservingUser: String?,
    reserved: MutableState<Boolean>,
    bought: MutableState<Boolean>,
    openDialog: MutableState<Boolean>,
    viewModel: AppViewModel
) {
    var item: Item?
    runBlocking {
        item = viewModel.getItemFromId(itemId)
    }
    val description = item?.description ?: AppContext.getContext()!!.getString(R.string.item_no_description)
    val url = item?.url ?: AppContext.getContext()!!.getString(R.string.item_no_link)
    val buyButtonText = if (reserved.value && bought.value) AppContext.getContext()!!.getString(R.string.btn_bought) else AppContext.getContext()!!.getString(R.string.btn_buy)
    val reserveButtonText = if (reserved.value) AppContext.getContext()!!.getString(R.string.btn_unreserve) else AppContext.getContext()!!.getString(R.string.btn_reserve)
    val price = if (item?.priceLowerBound != null && item?.priceUpperBound != null) {
        "€${item?.priceLowerBound} - €${item?.priceUpperBound}"
    } else {
        AppContext.getContext()!!.getString(R.string.item_no_price)
    }
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        DialogCard(
            minHeight = 300.dp,
            maxHeight = 600.dp,
            elevations = 10.dp,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                val entryPaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                DialogImage(imageDescription = AppContext.getContext()!!.getString(R.string.description_item_image), imageUri = image)
                DialogTitle(paddingValues = entryPaddingValues, text = itemName)
                if (url == AppContext.getContext()!!.getString(R.string.item_no_link)) {
                    DialogEntry(paddingValues = entryPaddingValues, text = "Link: ", value = url)
                } else {
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        text = AppContext.getContext()!!.getString(R.string.item_link),
                        value = {
                            OutlinedButton(
                                onClick = {
                                    val uri = Uri.parse(url)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    AppContext.getContext()?.startActivity(intent)
                                }
                            ) {
                                Text(text = AppContext.getContext()!!.getString(R.string.btn_open_link))
                            }
                        }
                    )
                }

                DialogEntry(
                    paddingValues = entryPaddingValues,
                    text = AppContext.getContext()!!.getString(R.string.item_price_range),
                    value = price
                )
                DialogEntry(
                    paddingValues = entryPaddingValues,
                    text = AppContext.getContext()!!.getString(R.string.item_description),
                    value = description
                )
                if (username != AppContext.getCurrentUser()) {
                    if (reservingUser != null) {
                        if (reservingUser != AppContext.getCurrentUser()) {
                            DialogEntry(
                                paddingValues = PaddingValues(horizontal = 15.dp),
                                text = AppContext.getContext()!!.getString(R.string.item_reserved_by),
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
                        } else {
                            DialogEntry(
                                paddingValues = PaddingValues(horizontal = 15.dp),
                                text = AppContext.getContext()!!.getString(R.string.item_reserved_by),
                                value = AppContext.getContext()!!.getString(R.string.item_you_reserved)
                            )
                        }
                    }
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        composable1 = {
                            Button(
                                onClick = {
                                    reserved.value = !reserved.value
                                    if (reserved.value) {
                                        viewModel.updateItem(itemId, reservedBy = AppContext.getCurrentUser())
                                    } else {
                                        viewModel.updateItem(itemId, reservedBy = "")
                                    }
                                },
                                enabled = !bought.value,
                                modifier = Modifier.size(120.dp, 45.dp)
                            ) {
                                Text(text = reserveButtonText)
                            }
                        },
                        composable2 = {
                            Button(
                                onClick = {
                                    bought.value = !bought.value
                                    if (bought.value) {
                                        viewModel.updateItem(itemId, bought = true)
                                    } else {
                                        viewModel.updateItem(itemId, bought = false)
                                    }
                                          },
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
