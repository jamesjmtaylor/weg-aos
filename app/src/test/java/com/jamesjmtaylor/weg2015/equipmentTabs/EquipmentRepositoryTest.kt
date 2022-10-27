package com.jamesjmtaylor.weg2015.equipmentTabs

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.jamesjmtaylor.weg2015.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
@Config(packageName = "com.jamesjmtaylor.weg2015")
class EquipmentRepositoryTest {
    private var app: App? = null

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
        setOfflineWebMock(json, app!!)//Crash immediately on null, this is a test after all...
        val gunLiveData = EquipmentRepository().getGun()

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