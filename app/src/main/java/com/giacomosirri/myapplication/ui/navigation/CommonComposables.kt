package com.giacomosirri.myapplication.ui.navigation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.*
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


const val REQUEST_IMAGE_CAPTURE = 1
fun getImageDirectory(): File {
    val imageDir = File(AppContext.getContext()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "GiftAlong")
    if (!imageDir.exists()) {
        imageDir.mkdirs()
    }
    return imageDir
}

val imageDir = getImageDirectory()
val locationForPhotos: Uri = FileProvider.getUriForFile(
    AppContext.getContext()!!,
    "com.giacomosirri.myapplication.provider",
    imageDir
)

fun capturePhoto(targetFileName : String) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        putExtra(MediaStore.EXTRA_OUTPUT, Uri.withAppendedPath(locationForPhotos, targetFileName))
    }
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(AppContext.getContext()!!, intent,  null)
}


val calendar: Calendar = Calendar.getInstance()
val currentYear = calendar.get(Calendar.YEAR)

fun getSpecialEventDate(year: Int, month: Int, day: Int): Date {
    calendar.set(year, month, day)
    if (calendar.time < Date()) {
        calendar[currentYear + 1, month] = day
    }
    return calendar.time
}

val specialEvents = mapOf(
    Pair(getSpecialEventDate(currentYear, Calendar.DECEMBER, 25), Pair("Christmas", Color.Red)),
    Pair(getSpecialEventDate(currentYear, Calendar.DECEMBER, 31), Pair("New Year's Eve", Color.Blue)),
    Pair(getSpecialEventDate(currentYear, Calendar.FEBRUARY, 14), Pair("Valentine's Day", Color.Magenta)),
    Pair(getSpecialEventDate(currentYear, Calendar.OCTOBER, 31), Pair("Halloween", Color.Cyan)),

    )

val specialDateFormat : SimpleDateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
val dateFormat : SimpleDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH)
val profileDateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

@Composable
fun DialogCard(
    minHeight: Dp,
    maxHeight: Dp,
    elevations: Dp,
    colors: CardColors,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.heightIn(max = maxHeight, min = minHeight),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevations,
            pressedElevation = elevations,
            disabledElevation = elevations,
            draggedElevation = elevations,
            focusedElevation = elevations,
            hoveredElevation = elevations
        ),
        border = border,
        colors = colors,
        content = content
    )
}

@Composable
fun DialogEntry(
    paddingValues: PaddingValues,
    text: String,
    value: String
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun DialogEntry(
    paddingValues: PaddingValues,
    text: String,
    value: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
        value()
    }
}

@Composable
fun DialogEntry(
    paddingValues: PaddingValues,
    composable1: @Composable () -> Unit,
    composable2: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        composable1()
        composable2()
    }
}

@Composable
fun DialogImage(
    imageDescription: String,
    imageId: Int
) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.35f),
        painter = painterResource(id = imageId),
        contentDescription = imageDescription,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun DialogTitle(
    paddingValues: PaddingValues,
    text: String
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = Typography.headlineLarge
    )
}

@Composable
fun PhotoSelector() {
    val image = remember { mutableStateOf<Bitmap?>(null) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = image.value?.asImageBitmap() ?: ImageBitmap.imageResource(id = R.drawable.placeholder),
            contentDescription = "Item image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredSize(width = 165.dp, height = 140.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        FilledTonalButton(
            modifier = Modifier.padding(start = 15.dp),
            onClick = {
                capturePhoto("test.png")
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                image.value = BitmapFactory.decodeFile(locationForPhotos.encodedPath + "/test.png", options)
            }
        ) {
            Icon(
                modifier = Modifier.padding(end = 5.dp),
                imageVector = ImageVector.vectorResource(R.drawable.round_camera_alt_24),
                contentDescription = null
            )
            Text("Select a photo")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    paddingValues: PaddingValues,
    buttonText: String,
    datePickerState: DatePickerState
) {
    // Date dialog
    val isDateDialogOpen = remember { mutableStateOf(false) }
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
        modifier = Modifier.padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
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
            Text(buttonText)
        }
    }
}

@Composable
private fun CommonErrorDialog(
    isDialogOpen: MutableState<Boolean>,
    onAccept: (() -> Unit)?,
    dialogTitle: String?,
    mainText: String,
    acceptText: String,
    refuseText: String?,
) {
    Dialog(
        onDismissRequest = { isDialogOpen.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .heightIn(130.dp, 400.dp),
            colors = CardDefaults.cardColors(containerColor = ErrorBackground)
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 10.dp, bottom = 10.dp)) {
                if (!dialogTitle.isNullOrEmpty()) {
                    Text(dialogTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(mainText)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { isDialogOpen.value = false },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(text = refuseText!!, color = Error)
                    }
                    TextButton(
                        onClick = {
                            isDialogOpen.value = false
                            onAccept?.invoke()
                        }
                    ) {
                        Text(text = acceptText, color = Error)
                    }
                }
            }
        }
    }
}

/**
 * A dialog that shows up when the user decides to interrupt an operation to go to another screen.
 * Such operations include, for example, the filling of a form page. This type of dialog must have
 * both the "Quit" option and the "OK" or "Accept" one.
 */
@Composable
fun QuitScreenDialog(
    isDialogOpen: MutableState<Boolean>,
    onQuit: (() -> Unit),
    dialogTitle: String?,
    mainText: String,
    quitText: String,
    stayText: String
) {
    CommonErrorDialog(
        isDialogOpen = isDialogOpen,
        onAccept = onQuit,
        dialogTitle = dialogTitle,
        mainText = mainText,
        acceptText = quitText,
        refuseText = stayText
    )
}

/**
 * A dialog that shows up when the user wants to perform a definitive operation that is going to delete
 * an item of the user, or even the whole user account. This type of dialog must have both the "Accept"
 * option and the "OK" or "Accept" one.
 */
@Composable
fun DefinitiveDeletionDialog(
    isDialogOpen: MutableState<Boolean>,
    onAccept: (() -> Unit),
    dialogTitle: String?,
    mainText: String,
    acceptText: String,
    refuseText: String
) {
    CommonErrorDialog(
        isDialogOpen = isDialogOpen,
        onAccept = onAccept,
        dialogTitle = dialogTitle,
        mainText = mainText,
        acceptText = acceptText,
        refuseText = refuseText
    )
}

@Composable
fun FormButtons(
    paddingValues: PaddingValues,
    isSubmitEnabled: Boolean,
    onSubmitClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .height(45.dp)
            .fillMaxWidth()
    ) {
        val isCancelDialogOpen = remember { mutableStateOf(false) }
        if (isCancelDialogOpen.value) {
            QuitScreenDialog(
                isDialogOpen = isCancelDialogOpen,
                onQuit = onCancelClick,
                dialogTitle = null,
                mainText = "Are you sure you want to quit? All your inputs will be lost.",
                quitText = "Quit anyway",
                stayText = "Cancel"
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .padding(end = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            onClick = { isCancelDialogOpen.value = true }
        ) {
            Text(text = "Quit")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp),
            enabled = isSubmitEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
            onClick = onSubmitClick
        ) {
            Text(text = "Done")
        }
    }
}

@Composable
fun CheckboxItem(text: String, paddingValues: PaddingValues, state: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp)
            .padding(paddingValues)
            .toggleable(
                value = state.value,
                onValueChange = { state.value = !state.value },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.value,
            onCheckedChange = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun EventCard(event: Event, navController: NavController, viewModel: AppViewModel) {
    val openDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 5.dp)
            .height(50.dp)
            .clickable { openDialog.value = true },
        border = BorderStroke(1.dp, Primary),
        colors = CardDefaults.cardColors(containerColor = EventCardBackground)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(event.name, Modifier.align(Alignment.Center))
        }
    }
    if (openDialog.value) {
        EventDialog(
            event.id!!,
            event.organizer,
            event.name,
            dateFormat.format(event.date),
            event.dressCode ?: "No dress code",
            openDialog,
            navController
        ) { viewModel.deleteEvent(event.id) }
    }
}

@Composable
fun EventDialog(
    id: Int,
    organizer: String,
    eventName: String,
    date: String,
    dressCode: String,
    openDialog: MutableState<Boolean>,
    navController: NavController,
    onEventDeletion: () -> Unit
) {
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        DialogCard(
            minHeight = 300.dp,
            maxHeight = 600.dp,
            elevations = 10.dp,
            colors = CardDefaults.cardColors(containerColor = Secondary)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val entryPaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                DialogImage(imageDescription = "Event Position", imageId = R.drawable.placeholder)
                DialogTitle(paddingValues = entryPaddingValues, text = eventName)
                if (organizer != AppContext.getCurrentUser()) {
                    DialogEntry(
                        paddingValues = PaddingValues(horizontal = 15.dp),
                        text = "Organizer: ",
                        value = {
                            OutlinedButton(
                                onClick = {
                                    openDialog.value = false
                                    navController.navigate(NavigationScreen.UserProfile.name + organizer)
                                }
                            ) {
                                Text(text = organizer)
                            }
                        })
                }
                DialogEntry(paddingValues = entryPaddingValues, text = "Date: ", value = date)
                DialogEntry(paddingValues = entryPaddingValues, text = "Dress Code: ", value = dressCode)
                if (organizer == AppContext.getCurrentUser()) {
                    val isCancelEventDialogOpen = remember { mutableStateOf(false) }
                    if (isCancelEventDialogOpen.value) {
                        DefinitiveDeletionDialog(
                            isDialogOpen = isCancelEventDialogOpen,
                            onAccept = {
                                isCancelEventDialogOpen.value = false
                                openDialog.value = false
                                onEventDeletion.invoke()
                            },
                            dialogTitle = "Event Deletion",
                            mainText = "Are you sure you want to delete this event? This action cannot be undone.",
                            acceptText = "Yes",
                            refuseText = "Cancel"
                        )
                    }
                    DialogEntry(
                        paddingValues = entryPaddingValues,
                        composable1 = {
                            OutlinedButton(
                                onClick = {
                                    openDialog.value = false
                                    navController.navigate(NavigationScreen.NewEvent.name + id)
                                },
                                modifier = Modifier.size(140.dp, 45.dp)) {
                                Text(text = "Edit Event")
                            }
                        },
                        composable2 = {
                            Button(
                                onClick = { isCancelEventDialogOpen.value = true },
                                modifier = Modifier.size(140.dp, 45.dp)) {
                                Text(text = "Delete Event")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SingleChoiceDialog(
    title: String,
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    openDialog: MutableState<Boolean>,
    confirmChoice: () -> Unit
) {
    Dialog(
        onDismissRequest = { openDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Column(Modifier.selectableGroup()) {
                    for (item in items) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .selectable(
                                    selected = (item == selected),
                                    onClick = { onSelected(item) },
                                    role = Role.RadioButton
                                )
                        ) {
                            RadioButton(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (item == selected),
                                onClick = null
                            )
                            Text(text = item)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(end = 10.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            openDialog.value = false
                            confirmChoice.invoke()
                        }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}