package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun UserProfileScreen(
    paddingValues: PaddingValues,
    username: String,
    onWishlistButtonClick: () -> Unit,
    viewModel: AppViewModel,
    navController: NavController
) {
    val openDialog = remember { mutableStateOf(false) }
    val relationshipTypes = listOf("Friend", "Family", "Partner", "Colleague", "None")
    val (selected, onSelected) = remember { mutableStateOf(relationshipTypes[0]) }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = "$username's Profile",
                hasSearchBar = false,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Wishlist button
            TextButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = onWishlistButtonClick
            ) {
                Text(text = "See $username's Wishlist", fontSize = 22.sp)
            }
            // Profile pic
            Image(
                painterResource(id = R.drawable.placeholder),
                contentDescription = "Profile pic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(width = 210.dp, height = 160.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            // Registration date
            Text(
                text = "Registered since 31/08/2023",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            // Relationship status
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Select relationship status: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = { openDialog.value = true }) {
                    Text(text = selected)
                }
            }
            // Upcoming events
            Column {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Common upcoming events: ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)
                }
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                }
            }
        }
        if (openDialog.value) {
            SingleChoiceDialog(title = "Select the relationship:", relationshipTypes, selected, onSelected, openDialog) {
                viewModel.updateRelationship(AppContext.getCurrentUser(), username, selected)
            }
        }
    }
}