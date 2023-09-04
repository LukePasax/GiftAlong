package com.giacomosirri.myapplication.ui.navigation

import android.content.Context
import android.content.Intent
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
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    appViewModel: AppViewModel,
    paddingValues: PaddingValues,
    onExit: () -> Unit,
    isInEditMode: Boolean,
    id: Int? = null
) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    var name: String? = null
    var description: String? = null
    var location: String? = null
    var dressCode: String? = null
    var date: Date? = null
    var friends: Boolean? = null
    var partners: Boolean? = null
    var family: Boolean? = null
    var colleagues: Boolean? = null
    if (isInEditMode) {
        // id must exist
        name = getNameFromEventId(id!!)
        description = getDescriptionFromEventId(id)
        dressCode = getDressCodeFromEventId(id)
        date = getDateFromEventId(id)
        friends = getFriendsAllowedFromEventId(id)
        partners = getPartnersAllowedFromEventId(id)
        family = getFamilyAllowedFromEventId(id)
        colleagues = getColleaguesAllowedFromEventId(id)
    }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName =
                if (isInEditMode) {
                    "Edit $name"
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
            val eventTitle = remember { mutableStateOf(name ?: "") }
            val eventDescription = remember { mutableStateOf(description ?: "") }
            val eventDressCode = remember { mutableStateOf(dressCode ?: "") }
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = date?.time ?: Date().time,
                // An event can only be scheduled for the future.
                selectableDates = object: SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return Date(utcTimeMillis).after(Date())
                    }
                }
            )
            val friendsAllowed = remember { mutableStateOf(friends ?: false) }
            val partnersAllowed = remember { mutableStateOf(partners ?: false) }
            val familyAllowed = remember { mutableStateOf(family ?: false) }
            val colleaguesAllowed = remember { mutableStateOf(colleagues ?:false) }
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
                value = eventDressCode.value,
                onValueChange = { eventDressCode.value = it },
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
                onSubmitClick = {
                    if (isInEditMode) {
                        appViewModel.editEvent(
                            id = id!!,
                            name = eventTitle.value.trim(),
                            date = Date(datePickerState.selectedDateMillis!!),
                            location = null,
                            organizer = AppContext.getCurrentUser(),
                            dressCode = eventDressCode.value.trim().ifEmpty { null },
                            friendsAllowed = friendsAllowed.value,
                            partnersAllowed = partnersAllowed.value,
                            familyAllowed = familyAllowed.value,
                            colleaguesAllowed = colleaguesAllowed.value
                        )
                    } else {
                        appViewModel.addEvent(
                            name = eventTitle.value.trim(),
                            date = Date(datePickerState.selectedDateMillis!!),
                            location = null,
                            organizer = AppContext.getCurrentUser(),
                            dressCode = eventDressCode.value.trim().ifEmpty { null },
                            friendsAllowed = friendsAllowed.value,
                            partnersAllowed = partnersAllowed.value,
                            familyAllowed = familyAllowed.value,
                            colleaguesAllowed = colleaguesAllowed.value
                        )
                    }
                    onExit.invoke()
                },
                onCancelClick = onExit
            )
        }
    }
}

private fun getDateFromEventId(id: Int): Date {
    return Date(0L)
}

private fun getColleaguesAllowedFromEventId(id: Int): Boolean {
    return true
}

private fun getFamilyAllowedFromEventId(id: Int): Boolean {
    return false
}

private fun getPartnersAllowedFromEventId(id: Int): Boolean {
    return true
}

private fun getFriendsAllowedFromEventId(id: Int): Boolean {
    return false
}

private fun getDressCodeFromEventId(id: Int): String {
    return "c"
}

private fun getDescriptionFromEventId(id: Int): String {
    return "b"
}

private fun getNameFromEventId(id: Int): String {
    return "a"
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