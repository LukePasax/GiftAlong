package com.giacomosirri.myapplication.ui.navigation

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.compose.foundation.border
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Error
import com.giacomosirri.myapplication.ui.theme.ErrorBackground
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    paddingValues: PaddingValues,
    onQuit: () -> Unit,
    isInEditMode: Boolean,
    eventName: String? = null
) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName =
                if (isInEditMode) {
                    "Edit $eventName"
                } else {
                    AppContext.getContext()?.getString(R.string.new_event)!!
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
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            var eventTitle by remember { mutableStateOf("") }
            var eventDescription by remember { mutableStateOf("") }
            var dressCode by remember { mutableStateOf("") }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Title *") },
            )
            // Location
            Box(modifier = Modifier
                .requiredHeight(120.dp)
                .requiredWidth(220.dp)
                .align(Alignment.CenterHorizontally)
                .border(width = 1.dp, shape = ShapeDefaults.Small, color = Color.Gray)
                .padding(lateralPadding)
                .padding(top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                FilledTonalButton(
                    enabled = canShowMap(),
                    onClick = { showMap() }
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 2.dp),
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null
                    )
                    Text("Add location")
                }
            }
            // Date dialog
            val isDateDialogOpen = remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
            if (isDateDialogOpen.value) {
                DatePickerDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onDismissRequest.
                        isDateDialogOpen.value = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { isDateDialogOpen.value = false },
                            enabled = confirmEnabled.value
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDateDialogOpen.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            // Date picker
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = DateFormat.getDateInstance().format(Date(datePickerState.selectedDateMillis!!))
                )
                FilledTonalButton(onClick = { isDateDialogOpen.value = true }) {
                    Icon(
                        modifier = Modifier.padding(end = 5.dp),
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null
                    )
                    Text("Select a date *")
                }
            }
            // Description
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth()
                    .height(120.dp),
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
            )
            // Invite
            Column(
                modifier = Modifier
                    .padding(lateralPadding)
                    .padding(top = 2.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "Invite *",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier
                        .padding(lateralPadding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val pv = PaddingValues(end = 30.dp)
                    Column(
                        modifier = Modifier.requiredWidth(150.dp),
                    ) {
                        CheckboxItem("Friends", pv)
                        CheckboxItem("Partner", pv)
                    }
                    Column(
                        modifier = Modifier.requiredWidth(150.dp),
                    ) {
                        CheckboxItem("Family", pv)
                        CheckboxItem("Colleagues", pv)
                    }
                }
            }
            // Dress code
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = dressCode,
                onValueChange = { dressCode = it },
                label = { Text("Dress code") },
            )
            // Buttons
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .padding(top = 18.dp)
                    .height(40.dp)
                    .fillMaxWidth()
            ) {
                val isCancelDialogOpen = remember { mutableStateOf(false) }
                if (isCancelDialogOpen.value) {
                    CancelDialog(isCancelDialogOpen, onQuit)
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(end = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                    onClick = { isCancelDialogOpen.value = true }
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

fun onSubmit(
    title: String?,
    location: Location?,
    date: Date?,
    description: String?,
    invitees: List<Relationship.RelationshipType>?,
    dressCode: String?
) {
    if (title == null || (invitees != null && invitees.isEmpty())) {
        /* TODO show error dialog */
    } else {
        /* TODO launch database insertion query */
    }
}

@Composable
fun CancelDialog(isCancelDialogOpen: MutableState<Boolean>, onQuit: () -> Unit) {
    Dialog(
        onDismissRequest = { isCancelDialogOpen.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(130.dp),
            colors = CardDefaults.cardColors(containerColor = ErrorBackground)
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 10.dp, bottom = 10.dp)) {
                Text("Are you sure you want to quit? All your inputs will be lost.")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { isCancelDialogOpen.value = false },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(text = "Cancel", color = Error)
                    }
                    TextButton(
                        onClick = {
                            isCancelDialogOpen.value = false
                            onQuit.invoke()
                        }
                    ) {
                        Text(text = "Quit anyway", color = Error)
                    }
                }
            }
        }
    }
}

fun canShowMap(): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)
    val context: Context = AppContext.getContext()!!
    return intent.resolveActivity(context.packageManager) != null
}

fun showMap() {
    val geoLocation = Uri.parse("geo:44.1391, 12.24315")
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = geoLocation
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val chooserIntent = Intent.createChooser(intent, "Open With")
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val context: Context = AppContext.getContext()!!
    startActivity(context, chooserIntent, null)
}

@Composable
fun CheckboxItem(text: String, paddingValues: PaddingValues) {
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