package com.giacomosirri.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.giacomosirri.myapplication.GiftAlong
import com.giacomosirri.myapplication.ui.navigation.NavigationApp
import com.giacomosirri.myapplication.ui.theme.MyApplicationTheme
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import com.giacomosirri.myapplication.viewmodel.AppViewModelFactory
import com.giacomosirri.myapplication.viewmodel.SettingsViewModel
import com.giacomosirri.myapplication.viewmodel.SettingsViewModelFactory
import com.google.android.gms.location.*
import java.lang.ref.WeakReference

class AppContext private constructor() {
    companion object {
        private lateinit var context: WeakReference<Context>
        private lateinit var username: String

        fun setContext(context: Context) {
            this.context = WeakReference(context.applicationContext)
        }

        fun getContext(): Context? = context.get()

        fun setCurrentUser(username: String) {
            this.username = username
        }

        fun getCurrentUser() = this.username
    }
}

class UserLocation private constructor() {
    companion object {
        private var latitude: Double = 0.0
        private var longitude: Double = 0.0

        fun setCoordinates(latitude: Double, longitude: Double) {
            this.latitude = latitude
            this.longitude = longitude
        }

        fun getCurrentLatitude(): Double = this.latitude

        fun getCurrentLongitude(): Double = this.longitude
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private val showAlertDialog = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted -> if (isGranted) startLocationUpdates()
        }
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                            .apply { setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL) }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                UserLocation.setCoordinates(p0.locations.last().latitude, p0.locations.last().longitude)
                stopLocationUpdates()
                requestingLocationUpdates = false
            }
        }

        val appViewModel by viewModels<AppViewModel> {
            val app = application as GiftAlong
            AppViewModelFactory(app.eventRepository, app.itemRepository, app.userRepository)
        }
        val settingsViewModel by viewModels<SettingsViewModel> {
            val app = application as GiftAlong
            SettingsViewModelFactory(app.settingsRepository)
        }
        setContent {
            AppContext.setContext(applicationContext)
            val darkModeOn by settingsViewModel.isDarkModeOn.collectAsState(initial = false)
            val isAutoAuthActive by settingsViewModel.isAutoAuthActive.collectAsState(initial = false)
            val authUser by settingsViewModel.authenticatedUser.collectAsState(initial = "")
            if (isAutoAuthActive) {
                AppContext.setCurrentUser(authUser)
            }
            MyApplicationTheme(darkModeOn) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationApp(
                        paddingValues = PaddingValues(top = 70.dp),
                        appViewModel = appViewModel,
                        settingsViewModel = settingsViewModel,
                        isUserLoggedIn = isAutoAuthActive
                    )
                }
            }
        }
    }

    fun startLocationUpdates() {
        requestingLocationUpdates = true
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION
        when {
            // Permission already granted.
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                } else {
                    showAlertDialog.value = true
                }
            }
            // Permission already denied. Do not do anything for now.
            shouldShowRequestPermissionRationale(permission) -> {}
            else -> {
                // First time: ask for permissions.
                locationPermissionRequest.launch(permission)
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}