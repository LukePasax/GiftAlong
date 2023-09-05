package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel

@Composable
fun RelationshipsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: AppViewModel,
    query: String?
) {
    val relationships = viewModel.getRelationshipsOfUser(AppContext.getCurrentUser()).collectAsState(initial = emptyList())
    if (query == null) {
        Scaffold(
            topBar = {
                NavigationAppBar(
                    currentScreenName = AppContext.getContext()
                        ?.getString(R.string.relationships)!!,
                    hasSearchBar = true,
                    onSearch = {
                        navController.popBackStack()
                        navController.navigate("${NavigationScreen.Relationships.name}?query=\"\"")
                    }
                )
            }
        ) {
            // Show only the users the current user has a relationship with.
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                for (relationship in relationships.value) {
                    item {
                        RelationshipListItem(
                            relationship.followed,
                            relationship.type.name,
                            navController,
                            viewModel
                        )
                    }
                }
            }
        }
    } else {
        SearchBar(
            searchBarPlaceholder = "Search any user of this app",
            currentScreen = NavigationScreen.Relationships.name
        ) {
            // Show all the users of the app that match the pattern with the right relationship with the current user.
            val allUsers = viewModel.getUsersMatchingPattern(query).collectAsState(initial = emptyList())
            val resultMap = mutableMapOf<String, String>()
            for (user in allUsers.value) {
                val relationship = relationships.value.find { it.followed == user.username }
                // The current user must not appear in the research.
                if (user.username != AppContext.getCurrentUser()) {
                    resultMap[user.username] = relationship?.type?.name ?: "None"
                }
            }
            LazyColumn {
                item {
                    for (elem in resultMap) {
                        RelationshipListItem(elem.key, elem.value, navController, viewModel, query)
                    }
                }
            }
        }
    }
}

@Composable
fun RelationshipListItem(
    username: String,
    relationshipType: String,
    navController: NavController,
    viewModel: AppViewModel,
    query: String? = null
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val relationshipTypes = listOf("Friend", "Family", "Partner", "Colleague", "None")
    val (selected, onSelected) = remember { mutableStateOf(relationshipType) }
    if (isDialogOpen.value) {
        SingleChoiceDialog(
            title = "Select the relationship:",
            relationshipTypes,
            selected,
            onSelected,
            isDialogOpen
        ) {
            viewModel.updateRelationship(AppContext.getCurrentUser(), username, selected)
            // Workaround to show the updated value of the relationship in the outlined button.
            if (query != null) {
                navController.popBackStack()
                navController.navigate("${NavigationScreen.Relationships.name}?query=$query")
            }
        }
    }
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clickable {
                    navController.navigate(NavigationScreen.UserProfile.name + username)
                },
            headlineContent = {
                Text(
                    text = username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            trailingContent = {
                OutlinedButton(onClick = { isDialogOpen.value = true }) {
                    Text(text = relationshipType, color = Color.Blue)
                }
            },
            leadingContent = {
                Image(
                    painterResource(id = R.drawable.placeholder),
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
}

@Composable
fun ListItemDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 15.dp),
        thickness = 1.dp,
        color = Color.Gray
    )
}
