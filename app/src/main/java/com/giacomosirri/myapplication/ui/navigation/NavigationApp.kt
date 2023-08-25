package com.giacomosirri.myapplication.ui.navigation

import com.giacomosirri.myapplication.ui.theme.Background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.giacomosirri.myapplication.R
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary
import kotlinx.coroutines.launch

sealed class NavigationScreen(val name: String) {
    object Home: NavigationScreen(AppContext.getContext()?.getString(R.string.main_page_title)!!)
    object Wishlist: NavigationScreen("Wishlist")
    object NewItem: NavigationScreen("New_item")
    object NewEventScreen: NavigationScreen("New_event")
    object UserProfileScreen: NavigationScreen("User_profile")
    object ItemScreen: NavigationScreen("Item")
    object EventScreen: NavigationScreen("Event")
}

class LeadingNavigationIconStrategy(val onBackArrow: () -> Unit, val onMenuIcon: () -> Unit)

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController(), paddingValues: PaddingValues) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.Home.name
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val strategy = LeadingNavigationIconStrategy({ navController.navigateUp()}, { scope.launch { drawerState.open() } })
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(.75f).fillMaxHeight(),
                drawerContainerColor = Background
            ) {
                val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
                val selectedItem = remember { mutableStateOf(items[0]) }
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = { NavigationAppBar(currentScreen, strategy) },
            floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new event")
                }
            },
        ) {
            NavigationGraph(navController = navController, paddingValues = paddingValues)
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Home.name,
    ) {
        composable(route = NavigationScreen.Home.name) {
            HomeScreen(paddingValues = paddingValues)
        }
        composable(route = NavigationScreen.Wishlist.name) {
            WishlistScreen(paddingValues = paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppBar(currentScreenTitle: String, strategy: LeadingNavigationIconStrategy) {
    CenterAlignedTopAppBar(
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search event"
                )
            }
        },
        title = {
            Text(
                text = currentScreenTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            when (currentScreenTitle) {
                AppContext.getContext()?.getString(R.string.main_page_title) ->
                    IconButton(onClick = strategy.onMenuIcon) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Main menu"
                        )
                    }
                else ->
                    IconButton(onClick = strategy.onBackArrow) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
    )
}