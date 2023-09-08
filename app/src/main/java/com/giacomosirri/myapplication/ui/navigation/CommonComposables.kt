package com.giacomosirri.myapplication.ui.navigation

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.work.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.*
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
    Pair(getSpecialEventDate(currentYear, Calendar.DECEMBER, 25), Pair(AppContext.getContext()!!.getString(R.string.christmas), Color.Red)),
    Pair(getSpecialEventDate(currentYear, Calendar.DECEMBER, 31), Pair(AppContext.getContext()!!.getString(R.string.new_year), Color(
        0xFF2196F3
    )
    )),
    Pair(getSpecialEventDate(currentYear, Calendar.FEBRUARY, 14), Pair(AppContext.getContext()!!.getString(R.string.valentines), Color.Magenta)),
    Pair(getSpecialEventDate(currentYear, Calendar.OCTOBER, 31), Pair(AppContext.getContext()!!.getString(R.string.halloween), Color(0xFFFF9800))),
)

val specialDateFormat : SimpleDateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
val dateFormat : SimpleDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH)
val profileDateFormat : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

@Composable
fun DialogCard(
    minHeight: Dp,
    maxHeight: Dp,
    elevations: Dp,
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
        Text(modifier = Modifier.padding(end = 8.dp), text = text, fontWeight = FontWeight.Bold)
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
        Text(modifier = Modifier.padding(end = 8.dp), text = text, fontWeight = FontWeight.Bold)
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
    imageUri: String?
) {
    if (imageUri != null) {
        AsyncImage(
            model = ImageRequest.Builder(AppContext.getContext()!!).data(Uri.parse(imageUri)).crossfade(true).build(),
            contentDescription = imageDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(.35f)
                .fillMaxWidth()
        )
    } else {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder),
            contentDescription = imageDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(.35f)
                .fillMaxWidth()
        )
    }
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
fun PhotoSelector(capturedImageUri: MutableState<Uri>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (capturedImageUri.value.path?.isNotEmpty() == true) {
            AsyncImage(
                model = ImageRequest.Builder(AppContext.getContext()!!).data(capturedImageUri.value).crossfade(true).build(),
                contentDescription = AppContext.getContext()!!.getString(R.string.description_item_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(width = 165.dp, height = 140.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        } else {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder),
                contentDescription = AppContext.getContext()!!.getString(R.string.description_item_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(width = 165.dp, height = 140.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }
        TakePhotoButton(
            modifier = Modifier.padding(start = 10.dp),
            capturedImageUri = capturedImageUri
        ) {
            Icon(
                modifier = Modifier.padding(end = 5.dp),
                imageVector = ImageVector.vectorResource(R.drawable.round_camera_alt_24),
                contentDescription = null
            )
            Text(AppContext.getContext()!!.getString(R.string.btn_photo_select))
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
                    Text(AppContext.getContext()!!.getString(R.string.btn_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { isDateDialogOpen.value = false }) {
                    Text(AppContext.getContext()!!.getString(R.string.btn_cancel))
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
                .padding(horizontal = 17.dp)
                .heightIn(130.dp, 400.dp),
        ) {
            Column(modifier = Modifier.padding(start = 15.dp, top = 20.dp, end = 12.dp, bottom = 10.dp)) {
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
                        Text(text = refuseText!!)
                    }
                    TextButton(
                        onClick = {
                            isDialogOpen.value = false
                            onAccept?.invoke()
                        }
                    ) {
                        Text(text = acceptText)
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
                mainText = AppContext.getContext()!!.getString(R.string.dialog_quit),
                quitText = AppContext.getContext()!!.getString(R.string.btn_quit_anyway),
                stayText = AppContext.getContext()!!.getString(R.string.btn_cancel)
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .requiredHeight(45.dp)
                .padding(end = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
            onClick = { isCancelDialogOpen.value = true }
        ) {
            Text(text = AppContext.getContext()!!.getString(R.string.btn_quit))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(45.dp)
                .padding(start = 5.dp),
            enabled = isSubmitEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
            onClick = onSubmitClick
        ) {
            Text(text = AppContext.getContext()!!.getString(R.string.btn_done))
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
            profileDateFormat.format(event.date),
            event.dressCode ?: AppContext.getContext()!!.getString(R.string.event_no_dress_code),
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
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val entryPaddingValues = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
                DialogImage(imageDescription = AppContext.getContext()!!.getString(R.string.event_position), imageUri = null)
                DialogTitle(paddingValues = entryPaddingValues, text = eventName)
                if (organizer != AppContext.getCurrentUser()) {
                    DialogEntry(
                        paddingValues = PaddingValues(horizontal = 15.dp),
                        text = AppContext.getContext()!!.getString(R.string.event_organizer),
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
                DialogEntry(paddingValues = entryPaddingValues, text = AppContext.getContext()!!.getString(R.string.event_date), value = date)
                DialogEntry(paddingValues = entryPaddingValues, text = AppContext.getContext()!!.getString(R.string.event_dress_code), value = dressCode)
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
                            dialogTitle = AppContext.getContext()!!.getString(R.string.dialog_event_delete_title),
                            mainText = AppContext.getContext()!!.getString(R.string.dialog_event_delete),
                            acceptText = AppContext.getContext()!!.getString(R.string.btn_yes),
                            refuseText = AppContext.getContext()!!.getString(R.string.btn_cancel)
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
                                Text(text = AppContext.getContext()!!.getString(R.string.btn_edit_event))
                            }
                        },
                        composable2 = {
                            Button(
                                onClick = { isCancelEventDialogOpen.value = true },
                                modifier = Modifier.size(140.dp, 45.dp)) {
                                Text(text = AppContext.getContext()!!.getString(R.string.btn_delete_event))
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
                            Text(text = AppContext.getContext()!!.getString(R.string.btn_ok))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TakePhotoButton(
    modifier: Modifier,
    capturedImageUri: MutableState<Uri>,
    content: @Composable RowScope.() -> Unit
) {
    val context = LocalContext.current
    val cameraOrGalleryLauncher = rememberLauncherForActivityResult(TakePictureFromCameraOrGalley()) {
        capturedImageUri.value = it ?: capturedImageUri.value
    }
    val permissionCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            cameraOrGalleryLauncher.launch(Unit)
        } else {
            Toast.makeText(context, AppContext.getContext()!!.getString(R.string.error_permission_denied), Toast.LENGTH_SHORT).show()
        }
    }
    if (capturedImageUri.value.path?.isNotEmpty() == true) {
        saveImage(capturedImageUri.value)
    }
    FilledTonalButton(
        modifier = modifier,
        onClick = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraOrGalleryLauncher.launch(Unit)
            } else {
                permissionCameraLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        content = content
    )
}

class TakePictureFromCameraOrGalley: ActivityResultContract<Unit, Uri?>() {
    private var photoUri: Uri? = null

    override fun createIntent(context: Context, input: Unit): Intent {
        return openImageIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent?.data ?: photoUri
    }

    private fun openImageIntent(context: Context): Intent {
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoUri = createPhotoTakenUri(context)
        // Write the captured image to a file.
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        val gallIntent = Intent(Intent.ACTION_GET_CONTENT)
        gallIntent.type = "image/*"
        // Look for available intents.
        val yourIntentsList = ArrayList<Intent>()
        val packageManager = context.packageManager
        packageManager.queryIntentActivities(camIntent, 0).forEach{
            val finalIntent = Intent(camIntent)
            finalIntent.component = ComponentName(it.activityInfo.packageName, it.activityInfo.name)
            yourIntentsList.add(finalIntent)
        }
        packageManager.queryIntentActivities(gallIntent, 0).forEach {
            val finalIntent = Intent(gallIntent)
            finalIntent.component = ComponentName(it.activityInfo.packageName, it.activityInfo.name)
            yourIntentsList.add(finalIntent)
        }
        val chooser = Intent.createChooser(gallIntent, AppContext.getContext()!!.getString(R.string.select_source))
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, yourIntentsList.toTypedArray())
        return chooser
    }

    private fun createPhotoTakenUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val file = File.createTempFile(imageFileName, ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(Objects.requireNonNull(context), context.packageName + ".provider", file)
    }
}

fun saveImage(capturedImageUri: Uri) {
    val workerBuilder = OneTimeWorkRequest.Builder(SaveImageWorker::class.java)
    val data = Data.Builder()
    data.putString("image_uri", capturedImageUri.toString())
    workerBuilder.setInputData(data.build())
    WorkManager.getInstance(AppContext.getContext()!!).enqueue(workerBuilder.build())
}

class SaveImageWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val uri = inputData.getString("image_uri")
        val contentResolver = AppContext.getContext()!!.applicationContext.contentResolver
        val bitmap = getBitmap(contentResolver, Uri.parse(uri))
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${SystemClock.uptimeMillis()}")
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream!!)
        outputStream.close()
        return Result.success()
    }
}

fun getBitmap(contentResolver: ContentResolver, capturedImageUri: Uri): Bitmap {
    return when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(contentResolver, capturedImageUri)
        else -> ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, capturedImageUri))
    }
}