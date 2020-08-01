package com.eyrafabdullayev.foursquareclone.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eyrafabdullayev.foursquareclone.R
import com.eyrafabdullayev.foursquareclone.model.PlaceModel
import kotlinx.android.synthetic.main.activity_new_place.*

lateinit var chosenImage: Bitmap

class NewPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_place)
    }

    fun selectImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 &&
            permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            var selected = data.data
            try {
                if(selected != null) {
                    chosenImage = if(Build.VERSION.SDK_INT > 28) {
                        val source = ImageDecoder.createSource(this.contentResolver,selected)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(this.contentResolver,selected)
                    }

                    imageView.setImageBitmap(chosenImage)
                }
            } catch (e: Exception) {
               Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun previous(view: View) {
        val intent = Intent(applicationContext, LocationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun next(view: View) {
        if (chosenImage != null) {
            val name = placeNameText.text.toString()
            val type = placeTypeText.text.toString()
            val atmosphere = placeAtmosphereText.text.toString()

            if(name.isNotEmpty() && type.isNotEmpty() && atmosphere.isNotEmpty()) {
                var placeObject =
                    PlaceModel(
                        name,
                        type,
                        atmosphere
                    )

                val intent = Intent(applicationContext,
                    MapsActivity::class.java)
                intent.putExtra("place",placeObject)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"Please Type All Required Fields!",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext,"Please Select an Image!",Toast.LENGTH_LONG).show()
        }
    }
}
