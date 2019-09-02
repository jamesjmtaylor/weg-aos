package com.jamesjmtaylor.weg2015

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.jamesjmtaylor.weg2015.models.daos.AirDao
import com.jamesjmtaylor.weg2015.models.daos.GunDao
import com.jamesjmtaylor.weg2015.models.daos.LandDao
import com.jamesjmtaylor.weg2015.models.daos.SeaDao
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea
import okhttp3.OkHttpClient


/**
 * Created by jtaylor on 2/14/18.
 */
class App : Application() {
    private var firebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    companion object {
        lateinit var INSTANCE: App
            private set
        val appWebClient: OkHttpClient by lazy { WebClient.getInstance() }
        val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(INSTANCE) }
    }

    //For testing purposes (TODO: Abstract to TestApplication extension)
    fun setAppDb(db: AppDatabase) {
        AppDatabase.setInstance(db)
    }

    fun setAppWebClient(client: OkHttpClient) {
        WebClient.setInstance(client)
    }
}

abstract class WebClient : OkHttpClient() {
    companion object {
        private var INSTANCE: OkHttpClient? = null
        fun getInstance(): OkHttpClient {
            if (INSTANCE == null) INSTANCE = OkHttpClient()
            return INSTANCE as OkHttpClient
        }

        fun setInstance(client: OkHttpClient) {
            INSTANCE = client
        }
    }
}

@Database(entities = arrayOf(Gun::class, Land::class, Air::class, Sea::class), version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GunDao(): GunDao
    abstract fun LandDao(): LandDao
    abstract fun AirDao(): AirDao
    abstract fun SeaDao(): SeaDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "weg-database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE as AppDatabase
        }

        fun setInstance(db: AppDatabase) {
            INSTANCE = db
        }
    }
}

