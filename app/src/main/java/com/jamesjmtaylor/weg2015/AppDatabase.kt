package com.jamesjmtaylor.weg2015

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.content.Context
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.Models.GunDao



/**
 * Created by jtaylor on 2/10/18.
 */
@Database(entities = arrayOf(Gun::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GunDao(): GunDao
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

