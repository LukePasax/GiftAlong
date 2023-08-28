package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.giacomosirri.myapplication.R


@Composable
fun UserProfileScreen(paddingValues: PaddingValues, username: String) {
    val openDialog = remember { mutableStateOf(false) }
    val relationshipTypes = listOf("Friend", "Family", "Partner", "Colleague")
    val (selected, onSelected) = remember { mutableStateOf(relationshipTypes[0]) }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "See $username's wishlist")
        }
        Image(painterResource(id = R.drawable.placeholder_foreground), contentDescription = "Profile picture", modifier = Modifier.size(100.dp))
        Text(text = "Registered since 2023")
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Select relationship status: ")
            OutlinedButton(onClick = { openDialog.value = true }) {
                Text(text = selected)
            }
        }
    }
    if (openDialog.value) {
        RelationshipDialog(relationshipTypes, selected, onSelected, openDialog)
    }
}


@Composable
fun RelationshipDialog(relationshipTypes: List<String>, selected: String, onSelected: (String) -> Unit, openDialog: MutableState<Boolean>) {
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Column(Modifier.selectableGroup()) {
                for (relationshipType in relationshipTypes) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = (relationshipType == selected),
                                onClick = { onSelected(relationshipType) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = (relationshipType == selected),
                            onClick = null
                        )
                        Text(text = relationshipType)
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}