package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R

@Composable
fun UserProfileScreen(paddingValues: PaddingValues, username: String, navController: NavController) {
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
                onClick = { navController.navigate(NavigationScreen.Wishlist.name + username) }
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
                    items(1) {
                        EventCard("Sergio's Degree")
                        EventCard("James' Birthday")
                    }
                }
            }
        }
        if (openDialog.value) {
            RadioButtonDialog(relationshipTypes, selected, onSelected, openDialog)
        }
    }
}

@Composable
fun RadioButtonDialog(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    openDialog: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            Column(Modifier.selectableGroup()) {
                for (item in items) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = (item == selected),
                                onClick = { onSelected(item) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            modifier = Modifier.padding(end = 6.dp),
                            selected = (item == selected),
                            onClick = null
                        )
                        Text(text = item)
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}