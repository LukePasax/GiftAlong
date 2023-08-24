package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.giacomosirri.myapplication.ui.theme.Primary

sealed class NavigationScreen(val name: String) {
    object Home: NavigationScreen("Home")
    object Wishlist: NavigationScreen("Wishlist")
    object NewItem: NavigationScreen("New_item")
    object NewEventScreen: NavigationScreen("New_event")
    object UserProfileScreen: NavigationScreen("User_profile")
    object ItemScreen: NavigationScreen("Item")
    object EventScreen: NavigationScreen("Event")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.Home.name
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenTitle = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                actionOnLeadingNavigationIcon = { navController.navigateUp() }
            )
        }
    ) {
            padding -> NavigationGraph(navController = navController, paddingValues = padding)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Home.name,
        modifier = modifier.padding(paddingValues)
    ) {
        composable(route = NavigationScreen.Home.name) {
            HomeScreen()
        }
    }
}

@Composable
fun NavigationAppBar(
    modifier: Modifier = Modifier,
    currentScreenTitle: String,
    canNavigateBack: Boolean,
    actionOnLeadingNavigationIcon: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
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
            IconButton(onClick = { actionOnLeadingNavigationIcon }) {
                if (canNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Main menu"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
    )
}