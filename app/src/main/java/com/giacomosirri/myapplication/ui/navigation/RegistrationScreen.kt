package com.giacomosirri.myapplication.ui.navigation

import android.net.Uri
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
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues,
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    onRegisterClick: () -> Unit
) {
    val lateralPadding = PaddingValues(horizontal = 20.dp)
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = AppContext.getContext()?.getString(R.string.registration)!!,
                hasSearchBar = false,
                isLeadingIconMenu = false,
                isLeadingIconBackArrow = true
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = 0L,
                // A person must be born before 2012 in order to be registered.
                selectableDates = object: SelectableDates {
                    override fun isSelectableYear(year: Int): Boolean {
                        return year < 2012
                    }
                }
            )
            // Name
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text("First name *") }
            )
            // Surname
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it },
                singleLine = true,
                label = { Text("Surname *") }
            )
            // Photo
            val capturedImageUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
            PhotoSelector(capturedImageUri)
            // Username
            OutlinedTextField(
                modifier = Modifier
                    .padding(lateralPadding)
                    .fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                singleLine = true,
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
            DateSelector(
                paddingValues = lateralPadding,
                buttonText = "Select your birthday *",
                datePickerState = datePickerState
            )
            // Register button
            Button(
                modifier = Modifier
                    .padding(lateralPadding)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                enabled = username.trim().isNotEmpty() && password.trim().isNotEmpty() && name.trim().isNotEmpty() && surname.trim().isNotEmpty(),
                onClick = {
                    val usernameExists = runBlocking { appViewModel.usernameExists(username.trim()) }
                    if (usernameExists) {
                        appViewModel.viewModelScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Username already exists. Please choose another one.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        appViewModel.registerUser(
                            username.trim(),
                            password.trim(),
                            name.trim(),
                            surname.trim(),
                            if (capturedImageUri.value.path?.isNotEmpty() == true) capturedImageUri.value.toString() else null,
                            Date(datePickerState.selectedDateMillis!!)
                        )
                        // The next time the user launches the app from this device,
                        // the login page won't show as the user is automatically authenticated.
                        // This is true until the user logs out.
                        settingsViewModel.activateAutomaticAuthentication(username.trim())
                        AppContext.setCurrentUser(username.trim())
                        onRegisterClick.invoke()
                    }
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}