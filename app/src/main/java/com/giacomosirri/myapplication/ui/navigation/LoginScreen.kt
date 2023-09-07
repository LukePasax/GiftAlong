package com.giacomosirri.myapplication.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.ui.theme.Typography
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: (Int) -> Unit
) {
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val passwordHidden = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoginButtonClicked = remember { mutableStateOf(false) }
    val showSplashScreen = remember { mutableStateOf(true) }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        LaunchedEffect(Unit) {
            delay(1500)
            showSplashScreen.value = false
        }
        if (showSplashScreen.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = AppContext.getContext()!!.getString(R.string.app_name),
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 44.sp,
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                ClickableText(
                    text = AnnotatedString(AppContext.getContext()!!.getString(R.string.btn_register)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp),
                    onClick = onRegisterClick,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AppContext.getContext()!!.getString(R.string.app_name),
                    style = Typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Username
                OutlinedTextField(
                    label = { Text(text = AppContext.getContext()!!.getString(R.string.label_username)) },
                    value = username.value,
                    singleLine = true,
                    onValueChange = { username.value = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Password
                OutlinedTextField(
                    label = { Text(text = AppContext.getContext()!!.getString(R.string.label_password)) },
                    value = password.value,
                    singleLine = true,
                    visualTransformation =
                    if (passwordHidden.value) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password.value = it },
                    trailingIcon = {
                        IconButton(onClick = { passwordHidden.value = !passwordHidden.value }) {
                            val visibilityIcon =
                                if (passwordHidden.value) Icons.Rounded.Visibility else Icons.Filled.VisibilityOff
                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordHidden.value) AppContext.getContext()!!.getString(R.string.description_password_show) else AppContext.getContext()!!.getString(R.string.description_password_hide)
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Check that the login is valid.
                if (isLoginButtonClicked.value) {
                    LaunchedEffect(Unit) {
                        val loginValid = appViewModel.loginUser(username.value.text, password.value.text)
                        if (loginValid) {
                            // Users passes the check and becomes the current user.
                            settingsViewModel.activateAutomaticAuthentication(username.value.text.trim())
                            AppContext.setCurrentUser(username.value.text.trim())
                            onLoginClick.invoke()
                        } else {
                            // User does not pass the check.
                            username.value = TextFieldValue()
                            password.value = TextFieldValue()
                            snackbarHostState.showSnackbar(
                                message = AppContext.getContext()!!.getString(R.string.error_wrong_credentials),
                                duration = SnackbarDuration.Short
                            )
                            isLoginButtonClicked.value = false
                        }
                    }
                }
                // Login button
                Box(modifier = Modifier.padding(horizontal = 40.dp)) {
                    Button(
                        onClick = { isLoginButtonClicked.value = true },
                        shape = RoundedCornerShape(50.dp),
                        enabled = username.value.text.trim()
                            .isNotEmpty() && password.value.text.trim()
                            .isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = AppContext.getContext()!!.getString(R.string.btn_login))
                    }
                }
            }
        }
    }
}