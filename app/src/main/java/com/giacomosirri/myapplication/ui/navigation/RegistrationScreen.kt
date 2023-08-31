package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext

@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues
) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = AppContext.getContext()?.getString(R.string.registration)!!,
                hasSearchBar = false,
                isLeadingIconMenu = false,
                isLeadingIconBackArrow = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var surname by remember { mutableStateOf("") }
            var passwordHidden by remember { mutableStateOf(true) }
            // Username
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                label = { Text("Username *") }
            )
            // Password
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Enter password") },
                visualTransformation =
                    if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val visibilityIcon = if (passwordHidden) Icons.Rounded.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            )
            Row(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
            ) {
                // Name
                OutlinedTextField(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .fillMaxWidth(.5f),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("First name *") }
                )
                // Surname
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth(),
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname *") }
                )
            }
            // Photo
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
    }
}