package com.jamesjmtaylor.weg2015

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.content.Context
import com.jamesjmtaylor.weg2015.models.daos.AirDao
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.daos.GunDao
import com.jamesjmtaylor.weg2015.models.daos.LandDao
import com.jamesjmtaylor.weg2015.models.daos.SeaDao


/**
 * Created by jtaylor on 2/10/18.
 */
@Database(entities = arrayOf(Air::class), version = 2, exportSchema = false)
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
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

