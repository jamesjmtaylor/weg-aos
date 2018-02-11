package com.jamesjmtaylor.weg2015

import android.app.Application
import android.arch.persistence.room.Room
import com.jamesjmtaylor.weg2015.Models.AppDatabase

/**
 * Created by jtaylor on 2/10/18.
 */
class App : Application() {
    private var app : App? = null
    private var appDb : AppDatabase? = null
    fun getInstance(): App {
        if (app == null) {
            app = App()
        }
        return app as App
    }
    fun getAppDb(): AppDatabase {
        val app = getInstance()
        if (app.appDb == null){
            app.appDb = Room.databaseBuilder(
                    app.applicationContext,
                    AppDatabase::class.java, "app.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
        return app.appDb as AppDatabase
    }
}