package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
                currentScreen = currentScreen,
                navigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
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
            HomeScreen(onNextButton = {
                navController.navigate(NavigationScreen.Second.name)
            })
        }
        composable(route = NavigationScreen.Second.name) {
            SecondScreen(
                onNextButton = {
                    navController.navigate(NavigationScreen.Third.name)
                },
                onCancelButton = {
                    navigateHome(navController)
                }
            )
        }
        composable(route = NavigationScreen.Third.name) {
            ThirdScreen(onCancelButton = {
                navigateHome(navController)
            })
        }
    }
}

@Composable
fun NavigationAppBar(
    modifier: Modifier = Modifier,
    currentScreenTitle: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = currentScreenTitle) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back arrow")
                }
            } else {

            }
        }
    )
}