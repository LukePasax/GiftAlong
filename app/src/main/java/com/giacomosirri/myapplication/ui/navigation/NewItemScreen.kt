package com.giacomosirri.myapplication.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.launch
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
    val snackbarHostState = remember { SnackbarHostState() }
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp, bottom = 18.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
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
            val capturedImageUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
            PhotoSelector(capturedImageUri)
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
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Price range:")
                OutlinedTextField(
                    modifier = Modifier
                        .requiredWidth(115.dp)
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
                        .requiredWidth(115.dp)
                        .padding(PaddingValues(horizontal = 10.dp)),
                    value = upperBoundPrice.value,
                    onValueChange = { upperBoundPrice.value = it },
                    label = { Text("Upper") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    modifier = Modifier.padding(start = 7.dp),
                    text = "€",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
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
                    var isValid = true
                    if (itemLink.value.trim().isNotEmpty()) {
                        // Test that the URL provided by the user is valid.
                        val uri = Uri.parse(itemLink.value.trim())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        if (intent.resolveActivity(AppContext.getContext()!!.packageManager) == null) {
                            isValid = false
                            appViewModel.viewModelScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "The link you provided is not a valid URL. " +
                                            "Please input another one or remove it altogether.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                    if (isValid) {
                        if (isInEditMode) {
                            appViewModel.updateItem(
                                id = id!!,
                                name = itemName.value.trim(),
                                description = itemDescription.value.trim().ifEmpty { "" },
                                url = itemLink.value.trim().ifEmpty { "" },
                                image = if (capturedImageUri.value.path?.isNotEmpty() == true) capturedImageUri.value.toString() else null,
                                priceL = try {
                                    lowerBoundPrice.value.toInt()
                                } catch (e: NumberFormatException) {
                                    -1
                                },
                                priceU = try {
                                    upperBoundPrice.value.toInt()
                                } catch (e: NumberFormatException) {
                                    -1
                                },
                            )
                        } else {
                            appViewModel.addItem(
                                name = itemName.value.trim(),
                                description = itemDescription.value.trim().ifEmpty { null },
                                url = itemLink.value.trim().ifEmpty { null },
                                image = if (capturedImageUri.value.path?.isNotEmpty() == true) capturedImageUri.value.toString() else null,
                                priceL = try {
                                    lowerBoundPrice.value.toInt()
                                } catch (e: NumberFormatException) {
                                    null
                                },
                                priceU = try {
                                    upperBoundPrice.value.toInt()
                                } catch (e: NumberFormatException) {
                                    null
                                },
                                listedBy = AppContext.getCurrentUser()
                            )
                        }
                        onExit.invoke()
                    }
                },
                onCancelClick = onExit
            )
        }
    }
}
