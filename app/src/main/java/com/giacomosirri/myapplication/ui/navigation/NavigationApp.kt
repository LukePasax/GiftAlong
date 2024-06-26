package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Typography
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

sealed class NavigationScreen(val name: String) {
    object Home: NavigationScreen(AppContext.getContext()?.getString(R.string.title_home)!!)
    object Wishlist: NavigationScreen(AppContext.getContext()?.getString(R.string.title_wishlist)!!)
    object NewItem: NavigationScreen(AppContext.getContext()?.getString(R.string.title_new_item)!!)
    object NewEvent: NavigationScreen(AppContext.getContext()?.getString(R.string.title_new_event)!!)
    object UserProfile: NavigationScreen(AppContext.getContext()?.getString(R.string.title_user_profile)!!)
    object Relationships: NavigationScreen(AppContext.getContext()?.getString(R.string.title_relationships)!!)
    object Login: NavigationScreen(AppContext.getContext()?.getString(R.string.title_login)!!)
    object Registration: NavigationScreen(AppContext.getContext()?.getString(R.string.title_registration)!!)
    object NewProfilePic: NavigationScreen(AppContext.getContext()?.getString(R.string.title_new_profile_pic)!!)
}

class Navigation(
    val navController: NavHostController,
    val drawerState: DrawerState,
    val scope: CoroutineScope,
)

class Navigator private constructor() {
    companion object {
        private lateinit var navigation: Navigation
        fun setNavigation(navigation: Navigation) { this.navigation = navigation }
        fun getNavigation(): Navigation = navigation
    }
}

class LeadingNavigationIconStrategy(val onBackArrow: () -> Unit, val onMenuIcon: () -> Unit)

@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController(),
    paddingValues: PaddingValues,
    isUserLoggedIn: Boolean,
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    darkModeOn: Boolean
) {
    Navigator.setNavigation(Navigation(
        navController = navController,
        drawerState = rememberDrawerState(DrawerValue.Closed),
        scope = rememberCoroutineScope(),
    ))
    val entryScreenName = navController.currentBackStackEntryAsState().value?.destination?.route ?:
        if (isUserLoggedIn) NavigationScreen.Home.name else NavigationScreen.Login.name
    if (entryScreenName != NavigationScreen.Login.name) {
        NavigationDrawer(appViewModel, settingsViewModel, darkModeOn, entryScreenName == NavigationScreen.Home.name) {
            Scaffold {
                NavigationGraph(
                    navController = navController,
                    paddingValues = paddingValues,
                    appViewModel = appViewModel,
                    settingsViewModel = settingsViewModel,
                    isLoginRequired = !isUserLoggedIn,
                )
            }
        }
    } else {
        NavigationGraph(
            navController = navController,
            paddingValues = paddingValues,
            appViewModel = appViewModel,
            settingsViewModel = settingsViewModel,
            isLoginRequired = !isUserLoggedIn,
        )
    }
}

@Composable
private fun NavigationDrawer(
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    darkModeOn: Boolean,
    canOpenDrawer: Boolean,
    content: @Composable () -> Unit
) {
    val navigation = Navigator.getNavigation()
    ModalNavigationDrawer(
        drawerState = navigation.drawerState,
        gesturesEnabled = canOpenDrawer,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .fillMaxHeight(),
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                val items = mapOf(
                    Pair(
                        AppContext.getContext()?.getString(R.string.menu_item_wishlist),
                        ImageVector.vectorResource(R.drawable.round_card_giftcard_24)
                    ),
                    Pair(
                        AppContext.getContext()?.getString(R.string.menu_item_relationships),
                        Icons.Rounded.Favorite
                    ),
                    Pair(
                        AppContext.getContext()?.getString(R.string.menu_item_users),
                        ImageVector.vectorResource(R.drawable.round_group_24)
                    ),
                    Pair(
                        AppContext.getContext()?.getString(R.string.menu_item_change_profile_pic),
                        ImageVector.vectorResource(R.drawable.round_camera_alt_24)
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.08f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AppContext.getContext()?.getString(R.string.app_name).toString(),
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
                            navigation.scope.launch { navigation.drawerState.close() }
                            selectedItem.value = item.key
                            navigateFromDrawer(selectedItem.value, navigation.navController)
                        },
                        modifier = Modifier.padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
                    )
                }
                Spacer(Modifier.fillMaxHeight(.62f))
                var darkMode by remember { mutableStateOf(darkModeOn) }
                Row(
                    modifier = Modifier.padding(start = 20.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(AppContext.getContext()!!.getString(R.string.btn_dark_mode))
                    Spacer(modifier = Modifier.fillMaxWidth(.8f))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = {
                            darkMode = it
                            if (darkMode) settingsViewModel.activateDarkMode() else settingsViewModel.deactivateDarkMode()
                        }
                    )
                }
                val isLogoutDialogOpen = remember { mutableStateOf(false) }
                if (isLogoutDialogOpen.value) {
                    QuitScreenDialog(
                        isDialogOpen = isLogoutDialogOpen,
                        onQuit = {
                            navigation.scope.launch { navigation.drawerState.close() }
                            settingsViewModel.deactivateAutomaticAuthentication()
                            navigation.navController.navigate(NavigationScreen.Login.name)
                        },
                        dialogTitle = AppContext.getContext()!!.getString(R.string.dialog_logout_title),
                        mainText = AppContext.getContext()!!.getString(R.string.dialog_logout),
                        quitText = AppContext.getContext()!!.getString(R.string.btn_yes),
                        stayText = AppContext.getContext()!!.getString(R.string.btn_dont_logout)
                    )
                }
                TextButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = {
                        isLogoutDialogOpen.value = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = AppContext.getContext()!!.getString(R.string.btn_logout), fontSize = 16.sp)
                }
                val isDeleteAccountDialogOpen = remember { mutableStateOf(false) }
                if (isDeleteAccountDialogOpen.value) {
                    QuitScreenDialog(
                        isDialogOpen = isDeleteAccountDialogOpen,
                        onQuit = {
                            appViewModel.unregisterUser(AppContext.getCurrentUser())
                            navigation.scope.launch { navigation.drawerState.close() }
                            navigation.navController.navigate(NavigationScreen.Login.name)
                        },
                        dialogTitle = AppContext.getContext()!!.getString(R.string.dialog_account_delete_title),
                        mainText = AppContext.getContext()!!.getString(R.string.dialog_account_delete),
                        quitText = AppContext.getContext()!!.getString(R.string.btn_yes),
                        stayText = AppContext.getContext()!!.getString(R.string.btn_dont_delete)
                    )
                }
                TextButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { isDeleteAccountDialogOpen.value = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = AppContext.getContext()!!.getString(R.string.btn_delete_account), fontSize = 16.sp)
                }
            }
        },
        content = content
    )
}

fun navigateFromDrawer(menuItem: String?, navController: NavHostController) {
    when(menuItem) {
        AppContext.getContext()?.getString(R.string.menu_item_wishlist) ->
            navController.navigate(NavigationScreen.Wishlist.name + AppContext.getCurrentUser())
        AppContext.getContext()?.getString(R.string.menu_item_relationships) ->
            navController.navigate(NavigationScreen.Relationships.name)
        AppContext.getContext()?.getString(R.string.menu_item_users) ->
            navController.navigate("${NavigationScreen.Relationships.name}?query=\"\"")
        AppContext.getContext()?.getString(R.string.menu_item_change_profile_pic) ->
            navController.navigate(NavigationScreen.NewProfilePic.name)
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    isLoginRequired: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoginRequired) NavigationScreen.Login.name else NavigationScreen.Home.name
    ) {
        composable(NavigationScreen.Registration.name) {
            RegistrationScreen(
                paddingValues = paddingValues,
                appViewModel = appViewModel,
                settingsViewModel = settingsViewModel,
                onRegisterClick = { navController.navigate(NavigationScreen.Home.name) }
            )
        }
        composable(NavigationScreen.Login.name) {
            LoginScreen(
                appViewModel = appViewModel,
                settingsViewModel = settingsViewModel,
                onLoginClick = { navController.navigate(NavigationScreen.Home.name) },
                onRegisterClick = { navController.navigate(NavigationScreen.Registration.name) }
            )
        }
        composable(NavigationScreen.Home.name) {
            HomeScreen(
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewEvent.name) },
                navController = navController,
                viewModel = appViewModel
            )
        }
        composable(NavigationScreen.Home.name + "{query}") {
            val query = it.arguments?.getString("query")
            HomeScreen(
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewEvent.name) },
                navController = navController,
                viewModel = appViewModel,
                query = query
            )
        }
        composable(
            route = "${NavigationScreen.Wishlist.name}{username}?query={query}",
            arguments = listOf(navArgument("query") { nullable = true })
        ) {
            val username = it.arguments?.getString("username") ?: ""
            val query = it.arguments?.getString("query")
            WishlistScreen(
                username = username,
                paddingValues = paddingValues,
                onFabClick = { navController.navigate(NavigationScreen.NewItem.name) },
                navController = navController,
                viewModel = appViewModel,
                query = query
            )
        }
        composable(NavigationScreen.NewItem.name) {
            NewItemScreen(
                appViewModel = appViewModel,
                paddingValues = paddingValues,
                onExit = { navController.navigateUp() },
                isInEditMode = false,
            )
        }
        composable(NavigationScreen.NewItem.name + "{itemId}") {
            val id = it.arguments?.getString("itemId")?.toInt()
            NewItemScreen(
                appViewModel = appViewModel,
                paddingValues = paddingValues,
                onExit = { navController.navigateUp() },
                isInEditMode = true,
                id = id
            )
        }
        composable(NavigationScreen.NewEvent.name) {
            NewEventScreen(
                appViewModel = appViewModel,
                paddingValues = paddingValues,
                onExit = { navController.navigateUp() },
                isInEditMode = false
            )
        }
        composable(NavigationScreen.NewEvent.name + "{eventId}") {
            val id = it.arguments?.getString("eventId")?.toInt()
            NewEventScreen(
                appViewModel = appViewModel,
                paddingValues = paddingValues,
                onExit = { navController.navigateUp() },
                isInEditMode = true,
                id = id
            )
        }
        composable(NavigationScreen.UserProfile.name + "{username}") {
            val username = it.arguments?.getString("username") ?: AppContext.getCurrentUser()
            UserProfileScreen(
                paddingValues = paddingValues,
                username = username,
                onWishlistButtonClick = { navController.navigate(NavigationScreen.Wishlist.name + username) },
                viewModel = appViewModel,
                navController = navController
            )
        }
        composable(
            route = "${NavigationScreen.Relationships.name}?query={query}",
            arguments = listOf(navArgument("query") { nullable = true })
        ) {
            val query = it.arguments?.getString("query")
            RelationshipsScreen(
                paddingValues = paddingValues,
                navController = navController,
                viewModel = appViewModel,
                query = query
            )
        }
        composable(NavigationScreen.NewProfilePic.name) {
            ChangeProfilePicScreen(
                paddingValues = paddingValues,
                viewModel = appViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppBar(
    currentScreenName: String,
    hasSearchBar: Boolean,
    onSearch: (() -> Unit)? = null,
    isLeadingIconMenu: Boolean = false,
    isLeadingIconBackArrow: Boolean = true
) {
    val navigation = Navigator.getNavigation()
    val navIconStrategy = LeadingNavigationIconStrategy(
        onBackArrow = { navigation.navController.navigateUp() },
        onMenuIcon = { navigation.scope.launch { navigation.drawerState.open() } }
    )
    CenterAlignedTopAppBar(
        actions = {
            // Only a handful of screens should have the search icon in the top app bar.
            if (hasSearchBar) {
                IconButton(onClick = onSearch!!) {
                    Icon(Icons.Filled.Search, AppContext.getContext()!!.getString(R.string.description_search_icon))
                }
            }
        },
        title = { Text(text = currentScreenName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            if (isLeadingIconMenu) {
                IconButton(onClick = navIconStrategy.onMenuIcon) {
                    Icon(Icons.Filled.Menu, AppContext.getContext()!!.getString(R.string.description_main_menu_icon))
                }
            }
            if (isLeadingIconBackArrow) {
                IconButton(onClick = navIconStrategy.onBackArrow) {
                    Icon(Icons.Filled.ArrowBack, AppContext.getContext()!!.getString(R.string.description_go_back_icon))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchBarPlaceholder: String,
    currentScreen: String,
    onGoBack: () -> Unit,
    onClear: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val navigation = Navigator.getNavigation()
    var query by remember { mutableStateOf("") }
    SearchBar(
        modifier = Modifier.fillMaxSize(),
        query = query,
        onQueryChange = { query = it },
        placeholder = { Text(searchBarPlaceholder) },
        onSearch = {
            navigation.navController.popBackStack()
            when (currentScreen) {
                NavigationScreen.Wishlist.name ->
                    navigation.navController.navigate("${NavigationScreen.Wishlist.name}${AppContext.getCurrentUser()}?query=$it" )
                NavigationScreen.Relationships.name ->
                    navigation.navController.navigate("${NavigationScreen.Relationships.name}?query=$it")
                NavigationScreen.Home.name ->
                    navigation.navController.navigate(NavigationScreen.Home.name + query)
            }
        },
        leadingIcon = {
            IconButton(onClick = onGoBack) {
                Icon(Icons.Rounded.ArrowBack, AppContext.getContext()!!.getString(R.string.description_close_icon))
            }
        },
        trailingIcon = {
            IconButton(onClick = onClear) {
                Icon(Icons.Rounded.Clear, AppContext.getContext()!!.getString(R.string.description_clear_icon))
            }
        },
        active = true,
        onActiveChange = {},
        shape = ShapeDefaults.ExtraSmall,
        content = content
    )
}