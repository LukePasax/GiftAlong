package com.giacomosirri.myapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
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
            Card(
                onClick = { /* Do something */ },
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight(.1f),
                border = BorderStroke(width = 2.dp, Primary)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Sergio's Degree", Modifier.align(Alignment.Center))
                }
            }
        }
    }
}