package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues,
    viewModel: AppViewModel,
    onRegisterClick: () -> Unit
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
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var surname by remember { mutableStateOf("") }
            var passwordHidden by remember { mutableStateOf(true) }
            // Name
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("First name *") }
            )
            // Surname
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname *") }
            )
            // Photo
            PhotoSelector()
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
                label = { Text("Password *") },
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
            // Date dialog
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
            DateDialog(
                paddingValues = lateralPadding,
                buttonText = "Select your birthday *",
                datePickerState = datePickerState
            )
            // Register button
            Button(
                modifier = Modifier.padding(lateralPadding).padding(top = 12.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                onClick = {
                    viewModel.registerUser(username, password, name, surname, Date(datePickerState.selectedDateMillis!!))
                    AppContext.setCurrentUser(username)
                    onRegisterClick.invoke()
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}