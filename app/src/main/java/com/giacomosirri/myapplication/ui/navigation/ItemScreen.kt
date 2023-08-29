package com.giacomosirri.myapplication.ui.navigation

import android.graphics.Outline
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Primary
import com.giacomosirri.myapplication.ui.theme.Secondary

@Composable
fun ItemScreen(paddingValues: PaddingValues, item : String, username : String) {
    val url = "www.url.it"
    val description = "This is a description"
    Scaffold(
        floatingActionButton = {
            if (AppContext.getCurrentUser() == username) {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit item")
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(bottom = 50.dp, top = 20.dp)
                .fillMaxHeight(),
            border = BorderStroke(1.dp, Primary),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
                pressedElevation = 10.dp,
                disabledElevation = 10.dp,
                draggedElevation = 10.dp,
                focusedElevation = 10.dp,
                hoveredElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Secondary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(1.dp, Color.Red),
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp
                )
                Image(
                    painterResource(id = R.drawable.placeholder_foreground),
                    contentDescription = "Item picture",
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, Color.Black)
                )
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Link: ", fontWeight = FontWeight.Bold)
                    Text(text = url)
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Price Range: ", fontWeight = FontWeight.Bold)
                    Text(text = "€ 10 - € 20")
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Description: ", fontWeight = FontWeight.Bold)
                    Text(text = description)
                }
            }
        }
    }
}