package com.eyrafabdullayev.foursquareclone.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eyrafabdullayev.foursquareclone.R
import com.eyrafabdullayev.foursquareclone.model.PlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.ByteArrayOutputStream

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var place: PlaceModel
    private lateinit var latitude: String
    private lateinit var longitude: String

    private var isUpdated: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.save_place, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_place) {
            save()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun save() {

        if (latitude != null && longitude != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            chosenImage!!.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()

            val parsFile = ParseFile("image.png", bytes)

            var parseObject = ParseObject("Location")
            parseObject.put("name", place.name)
            parseObject.put("type", place.type)
            parseObject.put("atmosphere", place.atmosphere)
            parseObject.put("username", ParseUser.getCurrentUser().username.toString())
            parseObject.put("latitude", latitude)
            parseObject.put("longitude", longitude)
            parseObject.put("image", parsFile)
            parseObject.saveInBackground { e ->
                if (e != null) {
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Location created", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, LocationActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please Select a Place!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        place = intent.getSerializableExtra("place") as PlaceModel
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener(myListener)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener {
            if (!isUpdated) {
                mMap.clear()
                val latLng = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1f,
                locationListener!!
            )

            mMap.clear()

            var lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var latLng = lastLocation?.longitude?.let { LatLng(lastLocation?.latitude, it) }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 &&
            permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
            grantResults.isNotEmpty()
        ) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1,
                    1f,
                    locationListener!!
                )

                mMap.clear()

                var lastLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                var latLng = lastLocation?.longitude?.let { LatLng(lastLocation?.latitude, it) }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private var myListener = GoogleMap.OnMapLongClickListener {
        mMap.clear()

        mMap.addMarker(MarkerOptions().position(it).title(place.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))

        latitude = it.latitude.toString()
        longitude = it.longitude.toString()

        Toast.makeText(applicationContext, "Now Save This Place!", Toast.LENGTH_LONG).show()
    }
}