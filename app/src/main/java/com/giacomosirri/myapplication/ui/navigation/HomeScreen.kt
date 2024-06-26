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
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.util.*

@Composable
fun HomeScreen(
    paddingValues: PaddingValues, 
    onFabClick: () -> Unit, 
    navController: NavController, 
    viewModel: AppViewModel,
    query: String? = null
) {
    val eventsOfUser = viewModel.getEventsOfUser(AppContext.getCurrentUser()).collectAsState(initial = emptyList())
    val eventsOrganized = viewModel.getEventsOrganizedByUser(AppContext.getCurrentUser()).collectAsState(initial = emptyList())
    val totalEvents: SortedMap<Date, List<Event>> = sortedMapOf()
    for (specialEvent in specialEvents) {
        val eventDate = specialEvent.key
        totalEvents[eventDate] = listOf()
    }
    for (event in eventsOfUser.value) {
        val eventDate = event.date
        if (totalEvents.containsKey(eventDate)) {
            totalEvents[eventDate] = totalEvents[eventDate]!!.plus(event)
        } else {
            totalEvents[eventDate] = listOf(event)
        }
    }
    for (event in eventsOrganized.value) {
        val eventDate = event.date
        if (totalEvents.containsKey(eventDate)) {
            totalEvents[eventDate] = totalEvents[eventDate]!!.plus(event)
        } else {
            totalEvents[eventDate] = listOf(event)
        }
    }
    if (query == null) {
        Scaffold(
            topBar = {
                NavigationAppBar(
                    currentScreenName = AppContext.getContext()?.getString(R.string.title_home)!!,
                    hasSearchBar = true,
                    onSearch = {
                        navController.popBackStack()
                        navController.navigate(NavigationScreen.Home.name + " ")
                    },
                    isLeadingIconMenu = true,
                    isLeadingIconBackArrow = false
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onFabClick) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = AppContext.getContext()!!.getString(R.string.description_fab_home_screen))
                }
            }
        ) {
            EventsList(
                paddingValues = paddingValues,
                totalEvents = totalEvents,
                navController = navController,
                viewModel = viewModel
            )
        }
    } else {
        val filteredEvents: SortedMap<Date, List<Event>> = sortedMapOf()
        totalEvents.forEach { (date, eventsList) ->
            val filteredList = eventsList.filter { event ->
                event.name.startsWith(query)
            }
            if (filteredList.isNotEmpty()) {
                filteredEvents[date] = filteredList
            }
        }
        SearchBar(
            searchBarPlaceholder = AppContext.getContext()!!.getString(R.string.search_hint_event),
            currentScreen = NavigationScreen.Home.name,
            onGoBack = {
                navController.popBackStack()
                navController.navigate(NavigationScreen.Home.name)
            },
            onClear = {
                navController.popBackStack()
                navController.navigate(NavigationScreen.Home.name + " ")
            }
        ) {
            EventsList(
                paddingValues = PaddingValues(0.dp),
                totalEvents = filteredEvents,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EventsList(
    paddingValues: PaddingValues,
    totalEvents: SortedMap<Date, List<Event>>,
    navController: NavController,
    viewModel: AppViewModel
) {
    LazyColumn(modifier = Modifier.padding(paddingValues).padding(top = 5.dp).padding(horizontal = 4.dp)) {
        for (date in totalEvents) {
            if (date.key in specialEvents.keys) {
                item {
                    SpecialEventCard(
                        date = specialDateFormat.format(date.key),
                        event = specialEvents[date.key]!!.first,
                        color = specialEvents[date.key]!!.second
                    )
                }
            }
            if (date.value.isEmpty()) continue
            item {
                DayCard(
                    date = dateFormat.format(date.key),
                    events = date.value,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun DayCard(date: String, events: List<Event>, navController: NavController, viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .defaultMinSize(minHeight = 100.dp)
    ) {
        Card(
            modifier = Modifier
                .height(35.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(date, Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
            }
        }
        for (event in events) {
            EventCard(event, navController, viewModel)
        }
    }
}

@Composable
fun SpecialEventCard(date: String, event: String, color: Color) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .height(75.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(contentColor = color, containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 13.sp,
            )
        }
    }
}

