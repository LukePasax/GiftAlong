package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewItemScreen(paddingValues: PaddingValues) {
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
                val pv = PaddingValues(end = 30.dp)
                CheckboxRow("Friends", pv)
                CheckboxRow("Partner", pv)
                CheckboxRow("Family", pv)
            }
        }
    }
}