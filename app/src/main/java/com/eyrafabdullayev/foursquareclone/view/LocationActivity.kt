package com.eyrafabdullayev.foursquareclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eyrafabdullayev.foursquareclone.R
import com.eyrafabdullayev.foursquareclone.adapter.RecyclerViewAdapter
import com.eyrafabdullayev.foursquareclone.model.PlaceModel
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_location.*
import kotlin.collections.ArrayList

class LocationActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var placeList = ArrayList<PlaceModel>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.new_place, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_place) {
            val intent = Intent(applicationContext, NewPlaceActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        getParseData()
    }

    private fun getParseData() {
        val query = ParseQuery.getQuery<ParseObject>("Location")
        query.whereEqualTo("username",ParseUser.getCurrentUser().username.toString())
        query.findInBackground { objects, e ->
            if (e != null) {
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (objects.isNotEmpty()) {
                    placeList.clear()
                    for (parseObject in objects) {
                        val name = parseObject.get("name") as String
                        val type = parseObject.get("type") as String
                        val atmosphere = parseObject.get("atmosphere") as String

                        val place = PlaceModel(name, type, atmosphere)
                        placeList.add(place)
                    }
                }
            }
        }

        //if there is not any place object in the placeList array
        placeList.add(
            PlaceModel(
                "Titanic",
                "Bar",
                "Friendly"
            )
        )

        recyclerViewAdapter =
            RecyclerViewAdapter(
                placeList,
                this,
                this
            )
        recyclerView.adapter = recyclerViewAdapter
    }

    override fun onItemClick(place: PlaceModel) {
        val intent = Intent(applicationContext,
            DetailsActivity::class.java)
        intent.putExtra("place",place)
        startActivity(intent)
    }
}