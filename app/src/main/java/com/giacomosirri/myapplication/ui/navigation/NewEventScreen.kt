package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(paddingValues: PaddingValues) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    Scaffold(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var eventTitle by remember { mutableStateOf("") }
            var eventDescription by remember { mutableStateOf("") }
            var dressCode by remember { mutableStateOf("") }
            // Title
            TextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Title") },
            )
            // Location
            Box(modifier = Modifier
                .padding(lateralPadding)
                .align(Alignment.CenterHorizontally)) {
                FilledTonalButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.padding(end = 2.dp),
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null
                    )
                    Text("Add location")
                }
            }
            // Date
            val openDialog = remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
            if (openDialog.value) {
                DatePickerDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onDismissRequest.
                        openDialog.value = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { openDialog.value = false },
                            enabled = confirmEnabled.value
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDialog.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            Row(modifier = Modifier
                .padding(lateralPadding)
                .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = DateFormat.getDateInstance().format(Date(datePickerState.selectedDateMillis!!))
                )
                FilledTonalButton(onClick = { openDialog.value = true }) {
                    Icon(
                        modifier = Modifier.padding(end = 2.dp),
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null
                    )
                    Text("Select a date")
                }
            }
            // Description
            TextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth()
                    .fillMaxHeight(.25f),
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
            )
            // Invite
            Column(modifier = Modifier.padding(lateralPadding)) {
                Text(
                    modifier = Modifier.padding(bottom = 3.dp),
                    text = "Invite:",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .fillMaxWidth()
                ) {
                    val pv = PaddingValues(end = 30.dp)
                    CheckboxRow("Friends", pv)
                    CheckboxRow("Partner", pv)
                    CheckboxRow("Family", pv)
                }
            }
            // Dress code
            TextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = dressCode,
                onValueChange = { dressCode = it },
                label = { Text("Dress code") },
            )
            Spacer(modifier = Modifier.fillMaxHeight(.7f))
            // Buttons
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(end = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}

@Composable
fun CheckboxRow(text: String, paddingValues: PaddingValues) {
    val (checkedState, onStateChange) = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
            .padding(paddingValues)
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