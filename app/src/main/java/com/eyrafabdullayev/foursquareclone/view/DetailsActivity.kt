package com.eyrafabdullayev.foursquareclone.view

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var chosenPlace: PlaceModel
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        chosenPlace = intent.getSerializableExtra("place") as PlaceModel
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            mMap = p0
        }

        val query = ParseQuery.getQuery<ParseObject>("Location")
        query.whereEqualTo("name",chosenPlace.name)
        query.findInBackground { objects, e ->
            if(e != null) {
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                if(objects.isNotEmpty()) {
                    for (parseObject in objects) {
                        val image = parseObject.get("image") as ParseFile
                        image.getDataInBackground { data, e ->
                            if(e != null) {
                                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
                            } else {
                                val bitmap = BitmapFactory.decodeByteArray(data,0,data.size)
                                placeImageView.setImageBitmap(bitmap)

                                val name = parseObject.get("name") as String
                                val type = parseObject.get("type") as String
                                val atmosphere = parseObject.get("atmosphere") as String
                                val latitude = parseObject.get("latitude") as String
                                val longitude = parseObject.get("longitude") as String

                                placeNameTextView.text = name
                                placeTypeTextView.text = type
                                placeAtmosphereTextView.text = atmosphere

                                val latLng = LatLng(latitude.toDouble(),longitude.toDouble())
                                mMap.addMarker(MarkerOptions().position(latLng).title(name))
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
                            }
                        }
                    }
                }
            }
        }
    }


}