package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giacomosirri.myapplication.R


@Composable
fun DialogEntry(text: String, value: String) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun DialogEntry(text: String, value: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
        value()
    }
}

@Composable
fun DialogEntry(composable1: @Composable () -> Unit, composable2: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        composable1()
        composable2()
    }
}

@Composable
fun DialogImage(imageDescription : String, imageId : Int) {
    Image(
        painterResource(id = imageId),
        contentDescription = imageDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.4f)
    )
}

@Composable
fun DialogText(text : String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}