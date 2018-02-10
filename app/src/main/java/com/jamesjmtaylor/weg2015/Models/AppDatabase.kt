package com.jamesjmtaylor.weg2015.Models

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by jtaylor on 2/10/18.
 */
@Database(entities = arrayOf(Gun::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun GunDao(): GunDao
}