package com.jamesjmtaylor.weg2015.equipmentTabs

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.os.Parcelable
import com.google.common.io.Resources.getResource
import com.jamesjmtaylor.weg2015.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.internal.Classes.getClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
@Config(packageName = "com.jamesjmtaylor.weg2015")
class EquipmentRepositoryTest {
    private var app : App? = null
    @Before
    fun createDB() {
        val context = RuntimeEnvironment.application.applicationContext
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        app = RuntimeEnvironment.application as? App
        app?.setAppDb(db)
    }
    @Test
    fun getGun() {
        val inputStream = javaClass.getResourceAsStream("getAllResponse.json")
        val json = getJsonFromInputStream(inputStream)
        setOfflineWebMock(json,app!!)//Crash immediately on null, this is a test after all...
        val gunLiveData = EquipmentRepository().getGun()
        ShadowApplication.runBackgroundTasks()

        val actual = gunLiveData.value
        val expected = "2B14; 82mm"
        assertEquals(expected, actual?.firstOrNull()?.name ?: NO_DATA)
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