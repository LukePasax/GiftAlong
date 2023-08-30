package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R


@Composable
fun UserProfileScreen(paddingValues: PaddingValues, username: String, navController: NavController) {
    val openDialog = remember { mutableStateOf(false) }
    val relationshipTypes = listOf("Friend", "Family", "Partner", "Colleague", "None")
    val (selected, onSelected) = remember { mutableStateOf(relationshipTypes[0]) }
    var username = "Sergio"
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(onClick = { navController.navigate(NavigationScreen.Wishlist.name + username) }) {
            Text(text = "See $username's wishlist")
        }
        Image(painterResource(id = R.drawable.placeholder), contentDescription = "Profile picture", modifier = Modifier.size(100.dp))
        Text(text = "Registered since 2023")
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Select relationship status: ")
            OutlinedButton(onClick = { openDialog.value = true }) {
                Text(text = selected)
            }
        }
        Text(text = "Common upcoming events: ")
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            items(1) {
                EventCard("Sergio's Degree")
                EventCard("James' Birthday")
            }
        }
    }
    if (openDialog.value) {
        RadioButtonDialog(relationshipTypes, selected, onSelected, openDialog)
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