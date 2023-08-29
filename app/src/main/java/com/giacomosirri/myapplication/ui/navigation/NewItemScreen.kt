package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R

@Composable
fun NewItemScreen(paddingValues: PaddingValues) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    Scaffold(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var itemName by remember { mutableStateOf("") }
            var itemDescription by remember { mutableStateOf("") }
            var itemLink by remember { mutableStateOf("") }
            var lowerBoundPrice by remember { mutableStateOf(0) }
            var upperBoundPrice by remember { mutableStateOf(0) }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Title") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Red,
                    unfocusedBorderColor = Color.Red,
                    focusedBorderColor = Color.Red,
                    focusedLabelColor = Color.Red,
                )
            )
            // Photo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 120.dp)
                        .padding(end = 20.dp),
                    painter = painterResource(id = R.drawable.placeholder_foreground),
                    contentDescription = "item image"
                )
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Select a new photo")
                }
            }
            // Link
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemLink,
                onValueChange = { itemLink = it },
                label = { Text("Link") },
            )
            // Price range
            val openDialog = remember { mutableStateOf(false) }
            val currencies = listOf("$", "€", "£")
            val (selected, onSelected) = remember { mutableStateOf(currencies[0]) }
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Price range:")
                OutlinedTextField(
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(PaddingValues(horizontal = 10.dp)),
                    value = lowerBoundPrice.toString(),
                    onValueChange = { lowerBoundPrice = it.toInt() },
                    label = { Text("Lower") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(text = "-")
                OutlinedTextField(
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(PaddingValues(horizontal = 10.dp)),
                    value = upperBoundPrice.toString(),
                    onValueChange = { upperBoundPrice = it.toInt() },
                    label = { Text("Upper") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedButton(
                    onClick = { openDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("$")
                }
            }
            if (openDialog.value) {
                RadioButtonDialog(currencies, selected, onSelected, openDialog)
            }
            // Description
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth()
                    .height(120.dp),
                value = itemDescription,
                onValueChange = { itemDescription = it },
                label = { Text("Description") },
            )
            // Buttons
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .padding(top = 50.dp)
                    .height(40.dp)
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