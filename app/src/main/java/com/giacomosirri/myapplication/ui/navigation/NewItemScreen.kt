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
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun NewItemScreen(
    appViewModel: AppViewModel,
    paddingValues: PaddingValues,
    onExit: () -> Unit,
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
        appViewModel.viewModelScope.launch {
            name = appViewModel.getItemNameFromId(id!!)
            description = appViewModel.getItemDescriptionFromId(id)
            link = appViewModel.getItemLinkFromId(id)
            upperBound = appViewModel.getItemUpperBoundPriceFromId(id)
            lowerBound = appViewModel.getItemLowerBoundPriceFromId(id)
        }
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
            val lowerBoundPrice = remember { mutableStateOf(lowerBound?: 0) }
            val upperBoundPrice = remember { mutableStateOf(upperBound ?: 0) }
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
                SingleChoiceDialog(title = "Select a currency:", currencies, selected, onSelected, openDialog)
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
                    if (isInEditMode) {
                        appViewModel.updateItem(
                            id = id!!,
                            name = itemName.value.trim().ifEmpty { null },
                            description = itemDescription.value.trim().ifEmpty { null },
                            url = itemDescription.value.trim().ifEmpty { null },
                            priceL = if (lowerBoundPrice.value == 0) lowerBoundPrice.value else null,
                            priceU = if (upperBoundPrice.value == 0) upperBoundPrice.value else null
                        )
                    } else {
                        appViewModel.addItem(
                            name = itemName.value.trim(),
                            description = itemDescription.value.trim().ifEmpty { null },
                            url = itemDescription.value.trim().ifEmpty { null },
                            priceL = if (lowerBoundPrice.value == 0) lowerBoundPrice.value else null,
                            priceU = if (upperBoundPrice.value == 0) upperBoundPrice.value else null,
                            listedBy = AppContext.getCurrentUser()
                        )
                    }
                    onExit.invoke()
                },
                onCancelClick = onExit
            )
        }
    }
}
