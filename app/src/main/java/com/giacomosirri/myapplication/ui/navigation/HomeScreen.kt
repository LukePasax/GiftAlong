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
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.*
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.*

val calendar = Calendar.getInstance()
val currentYear = calendar.get(Calendar.YEAR)

fun getSpecialEventDate(year: Int, month: Int, day: Int): Date {
    calendar.set(year, month, day)
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

@Composable
fun HomeScreen(paddingValues: PaddingValues, onFabClick: () -> Unit, navController: NavController, viewModel: AppViewModel) {
    val eventsOfUser = viewModel.getEventsOfUser(AppContext.getCurrentUser()).collectAsState(initial = listOf()).value
    val totalEvents : MutableMap<Date, List<Event>> = mutableMapOf()
    for (event in eventsOfUser) {
        val eventDate = event.date
        if (totalEvents.containsKey(eventDate)) {
            totalEvents[eventDate] = totalEvents[eventDate]!!.plus(event)
        } else {
            totalEvents[eventDate] = listOf(event)
        }
    }
    totalEvents.toSortedMap(compareBy { it.time })
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
            for (date in totalEvents.keys) {
                item {
                    val events = totalEvents[date]!!
                    if (specialEvents.containsKey(date)) {
                        val specialEvent = specialEvents[date]!!
                        SpecialEventCard(specialDateFormat.format(date), specialEvent.first, specialEvent.second)
                    }
                    DayCard(
                        date = dateFormat.format(date),
                        events = events.map { it.name },
                        navController = navController
                    )
                }
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
fun SpecialEventCard(date: String, event: String, color: Color) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .height(75.dp),
        border = BorderStroke(1.dp, Primary),
        colors = CardDefaults.cardColors(containerColor = color)
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

