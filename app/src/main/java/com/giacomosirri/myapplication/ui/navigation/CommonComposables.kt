package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.theme.Typography
import java.text.DateFormat
import java.util.*

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
        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
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
        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
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
        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
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
        modifier = Modifier.fillMaxWidth().fillMaxHeight(.35f),
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
        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = Typography.headlineLarge
    )
}

@Composable
fun PhotoSelector() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.placeholder),
            contentDescription = "Item image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredSize(width = 165.dp, height = 140.dp)
                .clip(RoundedCornerShape(5.dp))
        )
        FilledTonalButton(
            modifier = Modifier.padding(start = 15.dp),
            onClick = { /* TODO select an image from the gallery */ }
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
fun DateDialog(
    paddingValues: PaddingValues,
    buttonText: String
) {
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
        modifier = Modifier.padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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