package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.theme.Primary

@Composable
fun HomeScreen() {
    Column() {
        DayCard("Tuesday, December 21, 2023", listOf("Sergio's Degree", "James' Birthday"))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCard(date: String, events: List<String>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 10.dp)
            .defaultMinSize(minHeight = 100.dp)
    ) {
        Card(
            modifier = Modifier
                .height(35.dp),
            border = BorderStroke(1.dp, Primary),
            colors = CardDefaults.cardColors(containerColor = Primary)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(date, Modifier.align(Alignment.Center))
            }
        }
        for (event in events) {
            EventCard(event)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(event: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 5.dp)
            .height(50.dp),
        border = BorderStroke(1.dp, Primary)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(event, Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun WishlistScreen() {

}
