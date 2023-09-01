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
            var itemName by remember { mutableStateOf(name ?: "") }
            var itemDescription by remember { mutableStateOf(description ?:"") }
            var itemLink by remember { mutableStateOf(link ?: "") }
            var lowerBoundPrice by remember { mutableStateOf(upperBound?: 0) }
            var upperBoundPrice by remember { mutableStateOf(lowerBound ?: 0) }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Title *") }
            )
            // Photo
            PhotoSelector()
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
                    .height(130.dp),
                value = itemDescription,
                onValueChange = { itemDescription = it },
                label = { Text("Description") },
            )
            // Buttons
            FormButtons(
                paddingValues = PaddingValues(
                    start = lateralPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = 12.dp,
                    end = lateralPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
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

fun getLowerBoundPriceFromItemId(id: Int): Int {
    return 1
}

fun getUpperBoundPriceFromItemId(id: Int): Int {
    return 1
}

fun getLinkFromItemId(id: Int): String {
    return "Ciao"
}

fun getDescriptionFromItemId(id: Int): String {
    return "Ciaoo"
}

fun getNameFromItemId(id: Int): String {
    return "Ciaooo"
}
