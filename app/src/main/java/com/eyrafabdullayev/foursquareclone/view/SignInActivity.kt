package com.eyrafabdullayev.foursquareclone.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eyrafabdullayev.foursquareclone.R
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun signIn(view: View) {
       ParseUser.logInInBackground(usernameText.text.toString(),passwordText.text.toString()) { user, e ->
           if(e != null) {
               Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
           } else {
               if(user != null) {
                   Toast.makeText(applicationContext,"Welcome, " + user.username.toString(),Toast.LENGTH_LONG).show()
                   val intent = Intent(applicationContext,
                       LocationActivity::class.java)
                   startActivity(intent)
               }
           }
       }
    }

    fun signUp(view: View) {
        var user = ParseUser()
        user.username = usernameText.text.toString()
        user.setPassword(passwordText.text.toString())
        user.signUpInBackground { e ->
            if (e != null) {
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext,"User created",Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,
                    LocationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}