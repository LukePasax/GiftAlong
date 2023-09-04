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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.runBlocking

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
        runBlocking {
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
            val itemDescription = remember { mutableStateOf(description ?: "") }
            val itemLink = remember { mutableStateOf(link ?: "") }
            val lowerBoundPrice = remember { mutableStateOf(if (lowerBound != null) lowerBound.toString() else "") }
            val upperBoundPrice = remember { mutableStateOf(if (upperBound != null) upperBound.toString() else "") }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = itemName.value,
                onValueChange = { itemName.value = it },
                label = { Text("Title *") },
                singleLine = true
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
                singleLine = true
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
                    value = lowerBoundPrice.value,
                    onValueChange = { lowerBoundPrice.value = it },
                    label = { Text("Lower") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(text = "-")
                OutlinedTextField(
                    modifier = Modifier
                        .requiredWidth(100.dp)
                        .padding(PaddingValues(horizontal = 10.dp)),
                    value = upperBoundPrice.value,
                    onValueChange = { upperBoundPrice.value = it },
                    label = { Text("Upper") },
                    singleLine = true,
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
            val maxLength = 100
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth()
                    .height(130.dp),
                supportingText = {
                    Text(
                        text = "${itemDescription.value.length} / $maxLength",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },
                value = itemDescription.value,
                onValueChange = { if (it.length <= maxLength) itemDescription.value = it },
                label = { Text("Description") },
            )
            // Buttons
            FormButtons(
                paddingValues = PaddingValues(
                    start = lateralPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = 12.dp,
                    end = lateralPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                isSubmitEnabled = itemName.value.trim().isNotEmpty() && lowerBoundPrice.value <= upperBoundPrice.value,
                onSubmitClick = {
                    if (isInEditMode) {
                        appViewModel.updateItem(
                            id = id!!,
                            name = itemName.value.trim().ifEmpty { null },
                            description = itemDescription.value.trim().ifEmpty { null },
                            url = itemDescription.value.trim().ifEmpty { null },
                            priceL = try { lowerBoundPrice.value.toInt() } catch (e: NumberFormatException) { null },
                            priceU = try { upperBoundPrice.value.toInt() } catch (e: NumberFormatException) { null }
                        )
                    } else {
                        appViewModel.addItem(
                            name = itemName.value.trim(),
                            description = itemDescription.value.trim().ifEmpty { null },
                            url = itemDescription.value.trim().ifEmpty { null },
                            priceL = try { lowerBoundPrice.value.toInt() } catch (e: NumberFormatException) { null },
                            priceU = try { upperBoundPrice.value.toInt() } catch (e: NumberFormatException) { null },
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
