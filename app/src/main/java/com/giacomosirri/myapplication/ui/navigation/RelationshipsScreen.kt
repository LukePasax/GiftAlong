package com.giacomosirri.myapplication.ui.navigation

import android.net.Uri
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.data.entity.User
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel

@Composable
fun RelationshipsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: AppViewModel,
    query: String?
) {
    val relationships = viewModel.getRelationshipsOfUser(AppContext.getCurrentUser()).collectAsState(initial = emptyMap())
    if (query == null) {
        Scaffold(
            topBar = {
                NavigationAppBar(
                    currentScreenName = AppContext.getContext()
                        ?.getString(R.string.title_relationships)!!,
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
                            relationship.key.username,
                            relationship.value.name,
                            relationship.key.imageUri,
                            navController,
                            viewModel
                        )
                    }
                }
            }
        }
    } else {
        SearchBar(
            searchBarPlaceholder = AppContext.getContext()!!.getString(R.string.search_hint_user),
            currentScreen = NavigationScreen.Relationships.name,
            onGoBack = {
                navController.popBackStack()
                navController.navigate(NavigationScreen.Relationships.name)
            },
            onClear = {
                navController.popBackStack()
                navController.navigate("${NavigationScreen.Relationships.name}?query=\"\"")
            }
        ) {
            // Show all the users of the app that match the pattern with the right relationship with the current user.
            val allUsers = viewModel.getUsersMatchingPattern(query).collectAsState(initial = emptyList())
            val resultMap = mutableMapOf<User, String>()
            for (user in allUsers.value) {
                // The current user must not appear in the research.
                if (user.username != AppContext.getCurrentUser()) {
                    resultMap[user] = relationships.value[user]?.name ?:
                        Relationship.RelationshipType.stringOf(Relationship.RelationshipType.None)
                }
            }
            LazyColumn {
                item {
                    for (elem in resultMap) {
                        RelationshipListItem(elem.key.username, elem.value, elem.key.imageUri, navController, viewModel, query)
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
    profilePic: String?,
    navController: NavController,
    viewModel: AppViewModel,
    query: String? = null
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val relationshipTypes = listOf(
        AppContext.getContext()!!.getString(R.string.relationship_type_friend),
        AppContext.getContext()!!.getString(R.string.relationship_type_family),
        AppContext.getContext()!!.getString(R.string.relationship_type_partner),
        AppContext.getContext()!!.getString(R.string.relationship_type_colleague),
        AppContext.getContext()!!.getString(R.string.relationship_type_none))
    val (selected, onSelected) = remember { mutableStateOf(relationshipType) }
    if (isDialogOpen.value) {
        SingleChoiceDialog(
            title = AppContext.getContext()!!.getString(R.string.dialog_relationship_select),
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
                    Text(text = Relationship.RelationshipType.stringOf(Relationship.RelationshipType.aliasOf(relationshipType)), color = Color.Blue)
                }
            },
            leadingContent = {
                if (profilePic != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(AppContext.getContext()!!).data(Uri.parse(profilePic)).crossfade(true).build(),
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
}

@Composable
fun ListItemDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 15.dp),
        thickness = 1.dp,
        color = Color.Gray
    )
}
