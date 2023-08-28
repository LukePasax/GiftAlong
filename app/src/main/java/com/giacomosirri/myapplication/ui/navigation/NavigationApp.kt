package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Background
import com.giacomosirri.myapplication.ui.theme.Primary
import com.giacomosirri.myapplication.ui.theme.Typography
import kotlinx.coroutines.launch

val fromScreenNameToTitle = mapOf(
    Pair(AppContext.getContext()?.getString(R.string.home)!!, AppContext.getContext()?.getString(R.string.main_page_title)),
    Pair(AppContext.getContext()?.getString(R.string.wishlist)!!, null),
    Pair(AppContext.getContext()?.getString(R.string.new_item)!!, AppContext.getContext()?.getString(R.string.new_item_page_title)),
    Pair(AppContext.getContext()?.getString(R.string.new_event)!!, AppContext.getContext()?.getString(R.string.new_event_page_title)),
    Pair(AppContext.getContext()?.getString(R.string.specific_item)!!, null),
    Pair(AppContext.getContext()?.getString(R.string.specific_event)!!, null),
    Pair(AppContext.getContext()?.getString(R.string.relationships)!!, AppContext.getContext()?.getString(R.string.relationships_page_title)),
    Pair(AppContext.getContext()?.getString(R.string.data_center)!!, AppContext.getContext()?.getString(R.string.datacenter_page_title))
)

val screensWithSearchBars = listOf(
    AppContext.getContext()?.getString(R.string.home)!!,
    AppContext.getContext()?.getString(R.string.wishlist)!!,
    AppContext.getContext()?.getString(R.string.relationships)!!
)

sealed class NavigationScreen(val name: String) {
    object Home: NavigationScreen(AppContext.getContext()?.getString(R.string.home)!!)
    object Wishlist: NavigationScreen(AppContext.getContext()?.getString(R.string.wishlist)!!)
    object NewItem: NavigationScreen(AppContext.getContext()?.getString(R.string.new_item)!!)
    object NewEvent: NavigationScreen(AppContext.getContext()?.getString(R.string.new_event)!!)
    object UserProfile: NavigationScreen(AppContext.getContext()?.getString(R.string.user_profile)!!)
    object Item: NavigationScreen(AppContext.getContext()?.getString(R.string.specific_item)!!)
    object Event: NavigationScreen(AppContext.getContext()?.getString(R.string.specific_event)!!)
    object Relationships: NavigationScreen(AppContext.getContext()?.getString(R.string.relationships)!!)
    object DataCenter: NavigationScreen(AppContext.getContext()?.getString(R.string.data_center)!!)
    object Login: NavigationScreen(AppContext.getContext()?.getString(R.string.login)!!)
}

class LeadingNavigationIconStrategy(val onBackArrow: () -> Unit, val onMenuIcon: () -> Unit)

@Composable
fun NavigationApp(navController: NavHostController = rememberNavController(), paddingValues: PaddingValues) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.Login.name
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val strategy = LeadingNavigationIconStrategy({ navController.navigateUp() }, { scope.launch { drawerState.open() } })
    if (currentScreen != NavigationScreen.Login.name) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .fillMaxHeight(),
                    drawerContainerColor = Background
                ) {
                    val items = mapOf(
                        Pair(
                            AppContext.getContext()?.getString(R.string.menu_item1),
                            Icons.Rounded.Person
                        ),
                        Pair(
                            AppContext.getContext()?.getString(R.string.menu_item2),
                            Icons.Rounded.Favorite
                        ),
                        Pair(
                            AppContext.getContext()?.getString(R.string.menu_item3),
                            Icons.Rounded.FavoriteBorder
                        ),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.08f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = AppContext.getContext()?.getString(R.string.app_name).toString(),
                            color = Primary,
                            style = Typography.headlineLarge
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    val selectedItem: MutableState<String?> = remember { mutableStateOf(null) }
                    items.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.value, contentDescription = null) },
                            label = { item.key?.let { Text(it) } },
                            colors = NavigationDrawerItemDefaults.colors(),
                            selected = item.key == selectedItem.value,
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem.value = item.key
                                navigateFromDrawer(selectedItem.value, navController)
                            },
                            modifier = Modifier.padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
                        )
                    }
                    Spacer(Modifier.fillMaxHeight(.85f))
                    var darkMode by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.padding(start = 20.dp, end = 25.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark Mode")
                        Spacer(modifier = Modifier.fillMaxWidth(.8f))
                        Switch(checked = darkMode, onCheckedChange = { darkMode = it })
                    }
                }
            }
        ) {
            Scaffold(
                topBar = { NavigationAppBar(currentScreen, strategy) }
            ) {
                NavigationGraph(
                    navController = navController,
                    paddingValues = paddingValues,
                )
            }
        }
    } else {
        NavigationGraph(
            navController = navController,
            paddingValues = paddingValues,
        )
    }
}

fun navigateFromDrawer(menuItem: String?, navController: NavHostController) {
    when(menuItem) {
        AppContext.getContext()?.getString(R.string.menu_item1) ->
            navController.navigate(NavigationScreen.Wishlist.name)
        AppContext.getContext()?.getString(R.string.menu_item2) ->
            navController.navigate(NavigationScreen.Relationships.name)
        AppContext.getContext()?.getString(R.string.menu_item3) ->
            navController.navigate(NavigationScreen.UserProfile.name)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Login.name,
    ) {
        composable(NavigationScreen.Login.name) {
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            LoginScreen(
                username = username,
                password = password,
                onLoginClick = {
                    navController.navigate(NavigationScreen.Home.name)
                    AppContext.setCurrentUser(username = username.value.text)
                }
            )
        }
        composable(NavigationScreen.Home.name) {
            HomeScreen(
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewEvent.name) }
            )
        }
        composable(NavigationScreen.Wishlist.name) {
            WishlistScreen(
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewItem.name) }
            )
        }
        composable(NavigationScreen.NewItem.name) {
            NewItemScreen(paddingValues)
        }
        composable(NavigationScreen.NewEvent.name) {
            NewEventScreen(paddingValues)
        }
        composable(NavigationScreen.UserProfile.name) {
            UserProfileScreen(paddingValues, AppContext.getCurrentUser())
        }
        composable(NavigationScreen.Item.name) {
            ItemScreen(paddingValues)
        }
        composable(NavigationScreen.Event.name) {
            EventScreen(paddingValues)
        }
        composable(NavigationScreen.Relationships.name) {
            RelationshipsScreen(paddingValues)
        }
        composable(NavigationScreen.DataCenter.name) {
            DataCenterScreen(paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppBar(currentScreenName: String, strategy: LeadingNavigationIconStrategy) {
    var searchedEvent by remember { mutableStateOf("") }
    var searched by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    val isNavigationBarVisible by derivedStateOf { !isSearchBarVisible }
    if (isSearchBarVisible) {
        SearchBar(
            modifier = Modifier.fillMaxSize(),
            query = searchedEvent,
            onQueryChange = { searchedEvent = it },
            placeholder = { Text("Search an event") },
            onSearch = { searched = true },
            leadingIcon = {
                IconButton(onClick = { isSearchBarVisible = false }) {
                    Icon(Icons.Rounded.ArrowBack, "Close search bar")
                }
            },
            trailingIcon = {
                IconButton(onClick = { searchedEvent = "" }) {
                    Icon(Icons.Rounded.Clear, "Clear searched text")
                }
            },
            active = true,
            onActiveChange = {},
            colors = SearchBarDefaults.colors(containerColor = Background),
            shape = ShapeDefaults.ExtraSmall
        ) {
            if (searched) {
                HomeScreen(searchedEvent)
            }
        }
    }
    if (isNavigationBarVisible) {
        CenterAlignedTopAppBar(
            actions = {
                // Only a handful of screens should have the search icon in the top app bar.
                if (screensWithSearchBars.contains(currentScreenName)) {
                    IconButton(onClick = { isSearchBarVisible = true }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search event"
                        )
                    }
                }
            },
            title = {
                Text(
                    text = fromScreenNameToTitle[currentScreenName] ?: "This page has no title",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                when (currentScreenName) {
                    AppContext.getContext()?.getString(R.string.home) ->
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
}