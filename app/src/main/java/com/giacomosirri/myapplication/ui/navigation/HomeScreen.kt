package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.*

@Composable
fun HomeScreen(paddingValues: PaddingValues, onFabClick: () -> Unit, navController: NavController) {
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = AppContext.getContext()?.getString(R.string.home)!!,
                hasSearchBar = true,
                searchBarPlaceholder = "Search an event by its title",
                isLeadingIconMenu = true,
                isLeadingIconBackArrow = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new event")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(1) {
                DayCard(
                    date = "Tuesday, December 21, 2023",
                    events = listOf("Sergio's Degree", "James' Birthday"),
                    navController = navController
                )
                SpecialEventCard(date = "25 December", event = "Christmas")
            }
        }
    }
}

@Composable
fun HomeScreen(searchedEvents: String, navController: NavController) {
    LazyColumn(modifier = Modifier.padding(PaddingValues(top = 10.dp))) {
        items(1) {
            DayCard(
                date = "Tuesday, December 21, 2023",
                events = listOf("Sergio's Degree", "James' Birthday"),
                navController = navController
            )
        }
    }
}

@Composable
fun DayCard(date: String, events: List<String>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .defaultMinSize(minHeight = 100.dp)
    ) {
        Card(
            modifier = Modifier
                .height(35.dp),
            border = BorderStroke(1.dp, Primary),
            colors = CardDefaults.cardColors(containerColor = DayCardBackground)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(date, Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
            }
        }
        for (event in events) {
            EventCard(event, navController)
        }
    }
}

@Composable
fun SpecialEventCard(date: String, event: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .height(75.dp),
        border = BorderStroke(1.dp, Primary),
        colors = CardDefaults.cardColors(containerColor = SpecialEventCardBackground)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = event,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date,
                modifier = Modifier.align(Alignment.BottomCenter),
                color = Color.White,
                fontSize = 13.sp,
            )
        }
    }
}

