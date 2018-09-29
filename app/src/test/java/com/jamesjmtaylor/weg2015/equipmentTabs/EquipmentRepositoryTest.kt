package com.jamesjmtaylor.weg2015.equipmentTabs

import android.arch.persistence.room.Room
import android.content.Context
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.AppDatabase
import com.jamesjmtaylor.weg2015.getJsonFromInputStream
import com.jamesjmtaylor.weg2015.setOfflineWebMock
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(packageName = "com.jamesjmtaylor.weg2015")
class EquipmentRepositoryTest {
    private var app : App? = null
    @Before
    fun createDB() {
        val context = RuntimeEnvironment.application.applicationContext
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        app = RuntimeEnvironment.application as? App
        app?.appDbInstance = db
    }
    @Test
    fun getGun() {
        val inputStream = app
                ?.openFileInput("com/jamesjmtaylor/weg2015/testJson/getAllGunsResponse.json")
        val json = getJsonFromInputStream(inputStream)
        setOfflineWebMock(json,app!!)//Crash immediately on null, this is a test after all...
//        EquipmentRepository().
    }

    @Test
    fun getLand() {
    }

    @Test
    fun getSea() {
    }

    @Test
    fun getAir() {
    }

    @Test
    fun getAll() {
    }
}