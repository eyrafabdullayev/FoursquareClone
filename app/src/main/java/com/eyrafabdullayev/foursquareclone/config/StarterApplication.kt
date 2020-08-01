package com.eyrafabdullayev.foursquareclone.config

import android.app.Application
import com.parse.Parse

class StarterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("DoM6BUhAZtXPPQLjw8YnY0G5wBBaaZEjnTDhdee1")
            .clientKey("d3MlraG600D5eFbIifaiqq2u6lgmfOjBLKKjlADc")
            .server("https://parseapi.back4app.com/")
            .build())
    }
}