package com.giacomosirri.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.giacomosirri.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * An activity that displays a map showing the place at the device's current location.
 */
class MapActivity: AppCompatActivity(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Cesena, Italy) and default zoom to use when location permission is not granted.
    private val defaultLocation = LatLng(0.0, 0.0)
    private var locationPermissionGranted = false

    // The current location of the event for which this activity is launched. This activity
    // has the faculty of updating this value, in particular after the user clicks on the map.
    private var currentEventLocation: LatLng? = null

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastDeviceLocation: LatLng? = null

    private var markerPosition: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastDeviceLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        /*
        val button = findViewById<View>(R.id.button) as Button
        button.setOnClickListener {
            if (markerPosition != null) {
                val locationString = "${markerPosition!!.latitude},${markerPosition!!.longitude}"
                val data = Intent()
                data.putExtra("location", locationString)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
        */
        mapFragment!!.getMapAsync(this)
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastDeviceLocation)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.uiSettings.isMapToolbarEnabled = false
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        // Get the current location of the event as provided by the intent that launched this activity.
        // It's null if and only if the user has yet to select a location for the event.
        val eventCoordinates = intent.getStringExtra("location")
        if (eventCoordinates != null) {
            val latitude = eventCoordinates.split(",")[0].toDouble()
            val longitude = eventCoordinates.split(",")[1].toDouble()
            currentEventLocation = LatLng(latitude, longitude)
            markerPosition = currentEventLocation
            updateMapFocus()
        } else {
            // Prompt the user for permission to get the actual device's current location.
            getLocationPermission()
            if (locationPermissionGranted) {
                // Update the current location of the device.
                getDeviceLocation()
                // Turn on the My Location layer and the related control on the map.
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            }
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        // Request location permission, so that we can get the location of the device. The result
        // of the permission request is handled by a callback, onRequestPermissionsResult.
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                updateMapFocus()
            }
        }
    }

    /*
     * Moves the map to the proper position and potentially adds a marker on it.
     * If markerPosition is set, then this is the position and there needs to be a marker.
     * Otherwise, the map is moved to lastDeviceLocation and there must be no marker on it.
     * If even this location is not set due to the user not granting location permissions, then
     * the map focuses on a default location, with no marker on it.
     */
    private fun updateMapFocus() {
        val newPosition = if (markerPosition != null) markerPosition else if (lastDeviceLocation != null)
                            lastDeviceLocation else defaultLocation
        // The marker must be set only on certain conditions.
        if (markerPosition != null) {
            map?.addMarker(MarkerOptions().position(markerPosition!!))
        }
        // The camera must always be updated.
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition!!, DEFAULT_ZOOM.toFloat())
        map?.moveCamera(cameraUpdate)
        map?.addMarker(MarkerOptions().position(newPosition))
    }

    /**
     * Gets the current location of the device from GPS, then positions the map's camera so that
     * the current user position in is the center of the map and adds a marker on it.
     */
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        // Get the best and most recent location of the device, which may be null in the rare
        // cases when a location is not available.
        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val location = task.result
                lastDeviceLocation = LatLng(location.latitude, location.longitude)
                updateMapFocus()
            }
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}