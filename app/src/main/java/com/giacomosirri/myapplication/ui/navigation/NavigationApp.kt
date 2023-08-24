package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import com.giacomosirri.myapplication.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary

sealed class NavigationScreen(val name: String) {
    object Home: NavigationScreen(AppContext.getContext()?.getString(R.string.main_page_title)!!)
    object Wishlist: NavigationScreen("Wishlist")
    object NewItem: NavigationScreen("New_item")
    object NewEventScreen: NavigationScreen("New_event")
    object UserProfileScreen: NavigationScreen("User_profile")
    object ItemScreen: NavigationScreen("Item")
    object EventScreen: NavigationScreen("Event")
}

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController(), paddingValues: PaddingValues) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.Home.name
    Scaffold(
        topBar = {
            NavigationAppBar(currentScreenTitle = currentScreen)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    try {
                        navController.navigate(NavigationScreen.Wishlist.name)
                    } catch (e: java.lang.IllegalArgumentException) {
                        e.printStackTrace()
                    }
                }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new event"
                )
            }
        },
    ) {
        NavigationGraph(navController = navController, paddingValues = paddingValues)
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
fun NavigationAppBar(currentScreenTitle: String) {
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
            LeadingNavigationIcon(screenTitle = currentScreenTitle)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
    )
}

@Composable
fun LeadingNavigationIcon(screenTitle: String) {
    when (screenTitle) {
        AppContext.getContext()?.getString(R.string.main_page_title) ->
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Main menu"
                )
            }
        else ->
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
    }
}