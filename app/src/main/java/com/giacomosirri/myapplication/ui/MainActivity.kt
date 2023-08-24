package com.giacomosirri.myapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.ui.theme.MyApplicationTheme
import com.giacomosirri.myapplication.ui.theme.Primary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    HomeScreen(/*paddingValues = PaddingValues(horizontal = 0.dp, vertical = 0.dp)*/)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.padding(1.dp),
        topBar = {
            CenterAlignedTopAppBar(
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search event"
                        )
                    }
                },
                title = {
                    Text(
                        text = "Upcoming Events",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Main menu"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new event"
                )
            }
        },
    )
    {
        Column(
            modifier = Modifier.padding(it)
        ) {
            DayCard("Tuesday, December 21, 2023", listOf("Sergio's Degree", "James' Birthday"))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayCard(dat : String, events : List<String>) {
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
                Text(dat, Modifier.align(Alignment.Center))
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
