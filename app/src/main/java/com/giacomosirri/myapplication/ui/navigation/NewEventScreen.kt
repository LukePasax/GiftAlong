package com.giacomosirri.myapplication.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.giacomosirri.myapplication.ui.MapActivity
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
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
    var location: String? = null
    var dressCode: String? = null
    var date: Date? = null
    var friends: Boolean? = null
    var partners: Boolean? = null
    var family: Boolean? = null
    var colleagues: Boolean? = null
    if (isInEditMode) {
        // id must exist
        runBlocking {
            name = appViewModel.getEventNameFromId(id!!)
            dressCode = appViewModel.getEventDressCodeFromId(id)
            date = appViewModel.getEventDateFromId(id)
            friends = appViewModel.getFriendsParticipationToEventFromId(id)
            partners = appViewModel.getPartnersParticipationToEventFromId(id)
            family = appViewModel.getFamilyParticipationToEventFromId(id)
            colleagues = appViewModel.getColleaguesParticipationToEventFromId(id)
        }
    }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName =
                if (isInEditMode) {
                    AppContext.getContext()!!.getString(R.string.title_edit) + " $name"
                } else {
                    AppContext.getContext()?.getString(R.string.title_new_event)!!
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
                .padding(top = 16.dp, bottom = 18.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val eventTitle = remember { mutableStateOf(name ?: "") }
            val eventDressCode = remember { mutableStateOf(dressCode ?: "") }
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = date?.time?.plus(3600*2000) ?: Date().time,
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
                label = { Text(AppContext.getContext()!!.getString(R.string.label_title) + " *") },
                singleLine = true
            )
            // Location
            Box(modifier = Modifier
                .requiredHeight(170.dp)
                .requiredWidth(260.dp)
                .border(width = 1.dp, shape = ShapeDefaults.Small, color = Color.Gray)
                .padding(lateralPadding)
                .padding(top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                val mapActivityLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) {
                }
                FilledTonalButton(
                    enabled = canShowMap(),
                    onClick = { mapActivityLauncher.launch(Intent(AppContext.getContext()!!, MapActivity::class.java)) }
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 2.dp),
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null
                    )
                    Text(AppContext.getContext()!!.getString(R.string.btn_add_location))
                }
            }
            // Date dialog
            DateSelector(
                paddingValues = lateralPadding,
                buttonText = AppContext.getContext()!!.getString(R.string.btn_select_date) + " *",
                datePickerState = datePickerState
            )
            // Dress code
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = eventDressCode.value,
                onValueChange = { eventDressCode.value = it },
                label = { Text(AppContext.getContext()!!.getString(R.string.label_dress_code)) },
                singleLine = true
            )
            // Invite
            Column(
                modifier = Modifier
                    .padding(lateralPadding)
                    .padding(top = 2.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = AppContext.getContext()!!.getString(R.string.label_invite) + " *",
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
                        CheckboxItem(AppContext.getContext()!!.getString(R.string.relationship_type_friend) + "s", pv, friendsAllowed)
                        CheckboxItem(AppContext.getContext()!!.getString(R.string.relationship_type_partner), pv, partnersAllowed)
                    }
                    Column(
                        modifier = Modifier.requiredWidth(150.dp),
                    ) {
                        CheckboxItem(AppContext.getContext()!!.getString(R.string.relationship_type_family), pv, familyAllowed)
                        CheckboxItem(AppContext.getContext()!!.getString(R.string.relationship_type_colleague) + "s", pv, colleaguesAllowed)
                    }
                }
            }
            // Buttons
            FormButtons(
                paddingValues = PaddingValues(
                    start = lateralPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = 23.dp,
                    end = lateralPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                isSubmitEnabled = eventTitle.value.trim().isNotEmpty() &&
                        (friendsAllowed.value || partnersAllowed.value || familyAllowed.value || colleaguesAllowed.value),
                onSubmitClick = {
                    if (isInEditMode) {
                        appViewModel.updateEvent(
                            id = id!!,
                            name = eventTitle.value.trim(),
                            // The code below accounts for time zones when setting the date.
                            date = Date(datePickerState.selectedDateMillis!! + 1000 * ZoneId.systemDefault().rules.getOffset(
                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)).totalSeconds),                            location = null,
                            dressCode = eventDressCode.value.trim().ifEmpty { "" },
                            friendsAllowed = friendsAllowed.value,
                            partnersAllowed = partnersAllowed.value,
                            familyAllowed = familyAllowed.value,
                            colleaguesAllowed = colleaguesAllowed.value
                        )
                    } else {
                        appViewModel.addEvent(
                            name = eventTitle.value.trim(),
                            // The code below accounts for time zones when setting the date.
                            date = Date(datePickerState.selectedDateMillis!! + 1000 * ZoneId.systemDefault().rules.getOffset(
                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)).totalSeconds),
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
    val chooserIntent = Intent.createChooser(intent, AppContext.getContext()!!.getString(R.string.open_with))
    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val context: Context = AppContext.getContext()!!
    startActivity(context, chooserIntent, null)
}