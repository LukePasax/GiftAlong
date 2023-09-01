package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext

@Composable
fun NewItemScreen(
    paddingValues: PaddingValues,
    onQuit: () -> Unit,
    isInEditMode: Boolean,
    id: Int? = null
) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    var name: String? = null
    var description: String? = null
    var link: String? = null
    var upperBound: Int? = null
    var lowerBound: Int? = null
    if (isInEditMode) {
        // id must exist
        name = getNameFromItemId(id!!)
        description = getDescriptionFromItemId(id)
        link = getLinkFromItemId(id)
        upperBound = getUpperBoundPriceFromItemId(id)
        lowerBound = getLowerBoundPriceFromItemId(id)
    }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName =
                if (isInEditMode) {
                    "Edit $name"
                } else {
                    AppContext.getContext()?.getString(R.string.new_item)!!
                },
                hasSearchBar = false,
                isLeadingIconMenu = false,
                isLeadingIconBackArrow = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val itemName = remember { mutableStateOf(name ?: "") }
            val itemDescription = remember { mutableStateOf(description ?:"") }
            val itemLink = remember { mutableStateOf(link ?: "") }
            val lowerBoundPrice = remember { mutableStateOf(upperBound?: 0) }
            val upperBoundPrice = remember { mutableStateOf(lowerBound ?: 0) }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemName.value,
                onValueChange = { itemName.value = it },
                label = { Text("Title *") }
            )
            // Photo
            PhotoSelector()
            // Link
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemLink.value,
                onValueChange = { itemLink.value = it },
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
                    value = lowerBoundPrice.value.toString(),
                    onValueChange = { lowerBoundPrice.value = it.toInt() },
                    label = { Text("Lower") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(text = "-")
                OutlinedTextField(
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(PaddingValues(horizontal = 10.dp)),
                    value = upperBoundPrice.value.toString(),
                    onValueChange = { upperBoundPrice.value = it.toInt() },
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
                    .height(130.dp),
                value = itemDescription.value,
                onValueChange = { itemDescription.value = it },
                label = { Text("Description") },
            )
            // Buttons
            FormButtons(
                paddingValues = PaddingValues(
                    start = lateralPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = 12.dp,
                    end = lateralPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                isSubmitEnabled = itemName.value.trim().isNotEmpty(),
                onSubmitClick = {
                    if(isInEditMode) {
                        /* TODO: update the values in the database */
                    } else {
                        /* TODO: create a new item in the database */
                    }
                },
                onCancelClick = onQuit
            )
        }
    }
}

private fun getLowerBoundPriceFromItemId(id: Int): Int {
    return 1
}

private fun getUpperBoundPriceFromItemId(id: Int): Int {
    return 1
}

private fun getLinkFromItemId(id: Int): String {
    return "Ciao"
}

private fun getDescriptionFromItemId(id: Int): String {
    return "Ciaoo"
}

private fun getNameFromItemId(id: Int): String {
    return "Ciaooo"
}
