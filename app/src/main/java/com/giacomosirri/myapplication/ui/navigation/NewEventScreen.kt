package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun NewEventScreen(paddingValues: PaddingValues) {
    Scaffold(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var eventTitle by remember { mutableStateOf("") }
            var eventDescription by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Title") },
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.25f)
                    .padding(bottom = 8.dp),
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
            )
            Text(
                text = "Invite:",
                modifier = Modifier.padding(bottom = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                CheckboxRow("Friends")
                CheckboxRow("Partner")
                CheckboxRow("Family")
            }
        }
    }
}

@Composable
fun CheckboxRow(text: String) {
    val (checkedState, onStateChange) = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
            .padding(end = 30.dp)
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}