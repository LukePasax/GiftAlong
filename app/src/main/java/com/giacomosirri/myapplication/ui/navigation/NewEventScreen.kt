package com.giacomosirri.myapplication.ui.navigation

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.ui.AppContext
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
            verticalArrangement = Arrangement.spacedBy(17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val eventTitle = remember { mutableStateOf("") }
            val eventDescription = remember { mutableStateOf("") }
            val dressCode = remember { mutableStateOf("") }
            val friendsAllowed = remember { mutableStateOf(false) }
            val partnersAllowed = remember { mutableStateOf(false) }
            val familyAllowed = remember { mutableStateOf(false) }
            val colleaguesAllowed = remember { mutableStateOf(false) }
            // Title
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = eventTitle.value,
                onValueChange = { eventTitle.value = it },
                label = { Text("Title *") },
            )
            // Location
            Box(modifier = Modifier
                .requiredHeight(120.dp)
                .requiredWidth(220.dp)
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
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
            DateSelector(
                paddingValues = lateralPadding,
                buttonText = "Select a date *",
                datePickerState = datePickerState
            )
            // Description
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth()
                    .height(120.dp),
                value = eventDescription.value,
                onValueChange = { eventDescription.value = it },
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
                        CheckboxItem("Friends", pv, friendsAllowed)
                        CheckboxItem("Partner", pv, partnersAllowed)
                    }
                    Column(
                        modifier = Modifier.requiredWidth(150.dp),
                    ) {
                        CheckboxItem("Family", pv, familyAllowed)
                        CheckboxItem("Colleagues", pv, colleaguesAllowed)
                    }
                }
            }
            // Dress code
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = dressCode.value,
                onValueChange = { dressCode.value = it },
                label = { Text("Dress code") },
            )
            // Buttons
            FormButtons(
                paddingValues = PaddingValues(
                    start = lateralPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = 15.dp,
                    end = lateralPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                isSubmitEnabled = eventTitle.value.trim().isNotEmpty() &&
                        (friendsAllowed.value || partnersAllowed.value || familyAllowed.value || colleaguesAllowed.value),
                onSubmitClick = {},
                onCancelClick = onQuit
            )
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