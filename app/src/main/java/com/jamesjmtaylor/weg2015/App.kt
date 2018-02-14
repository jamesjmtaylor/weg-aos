package com.jamesjmtaylor.weg2015

import android.app.Application
import android.content.Context
import okhttp3.OkHttpClient

/**
 * Created by jtaylor on 2/14/18.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: App
            private set
    }
}