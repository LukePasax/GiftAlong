package com.giacomosirri.myapplication.ui.navigation

import com.giacomosirri.myapplication.ui.theme.Background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.giacomosirri.myapplication.ui.theme.Typography
import kotlinx.coroutines.launch

sealed class NavigationScreen(val name: String, val title: String?) {
    object Home: NavigationScreen(
        name = "Home",
        title = AppContext.getContext()?.getString(R.string.main_page_title)
    )
    object Wishlist: NavigationScreen(
        name = "Wishlist",
        title = null
    )
    object NewItem: NavigationScreen(
        name = "New item",
        title = AppContext.getContext()?.getString(R.string.new_item_page_title)
    )
    object NewEvent: NavigationScreen(
        name = "New event",
        title = AppContext.getContext()?.getString(R.string.new_item_page_title)
    )
    object UserProfile: NavigationScreen(
        name = "User profile",
        title = null
    )
    object Item: NavigationScreen(
        name = "Item",
        title = null
    )
    object Event: NavigationScreen(
        name = "Event",
        title = null
    )
    object Relationships: NavigationScreen(
        name = "Relationships",
        title = AppContext.getContext()?.getString(R.string.relationships_page_title)
    )
    object DataCenter: NavigationScreen(
        name = "Data center",
        title = AppContext.getContext()?.getString(R.string.datacenter_page_title)
    )
}

class LeadingNavigationIconStrategy(val onBackArrow: () -> Unit, val onMenuIcon: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
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
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .fillMaxHeight(),
                drawerContainerColor = Background
            ) {
                val items = mapOf(
                    Pair(AppContext.getContext()?.getString(R.string.menu_item1), Icons.Rounded.Person),
                    Pair(AppContext.getContext()?.getString(R.string.menu_item2), Icons.Rounded.Favorite),
                    Pair(AppContext.getContext()?.getString(R.string.menu_item3), Icons.Rounded.FavoriteBorder),
                )
                val selectedItem: MutableState<String?> = remember { mutableStateOf(null) }
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
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.value, contentDescription = null) },
                        label = { item.key?.let { Text(it) } },
                        colors = NavigationDrawerItemDefaults.colors(),
                        selected = item.key == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item.key
                        },
                        modifier = Modifier.padding(bottom = 6.dp)
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
        composable(NavigationScreen.Home.name) {
            HomeScreen(
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewEvent.name) }
            )
        }
        composable(NavigationScreen.Wishlist.name) {
            WishlistScreen(paddingValues)
        }
        composable(NavigationScreen.NewItem.name) {
            NewItemScreen(paddingValues)
        }
        composable(NavigationScreen.NewEvent.name) {
            NewEventScreen(paddingValues)
        }
        composable(NavigationScreen.UserProfile.name) {
            UserProfileScreen(paddingValues)
        }
        composable(NavigationScreen.Item.name) {
            ItemScreen(paddingValues)
        }
        composable(NavigationScreen.Event.name) {
            EventScreen(paddingValues)
        }
        composable(NavigationScreen.Relationships.name) {
            RelatioshipsScreen(paddingValues)
        }
        composable(NavigationScreen.DataCenter.name) {
            DataCenterScreen(paddingValues)
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
                "Home" ->
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