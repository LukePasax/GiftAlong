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
    viewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = AppContext.getContext()?.getString(R.string.relationships)!!,
                hasSearchBar = true,
                searchBarPlaceholder = "Search any user of this app"
            )
        }
    ) {
        val relationships = viewModel.getRelationshipsOfUser(AppContext.getCurrentUser())
            .collectAsState(initial = emptyList())
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            for (relationship in relationships.value) {
                item {
                    RelationshipListItem(relationship.followed, relationship.type.name, navController)
                }
            }
        }
    }
}

@Composable
fun RelationshipsScreen(searchedUsers: String) {
}

@Composable
fun RelationshipListItem(
    username: String,
    relationshipType: String,
    navController: NavController
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val relationshipTypes = listOf("Friend", "Family", "Partner", "Colleague")
    val (selected, onSelected) = remember { mutableStateOf(relationshipType) }
    if (isDialogOpen.value) {
        SingleChoiceDialog(title = "Select the relationship:", relationshipTypes, selected, onSelected, isDialogOpen)
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
