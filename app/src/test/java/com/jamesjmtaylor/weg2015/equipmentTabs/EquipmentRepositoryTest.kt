package com.jamesjmtaylor.weg2015.equipmentTabs

import android.app.Instrumentation
import android.arch.persistence.room.Room

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import org.robolectric.Robolectric
import com.jamesjmtaylor.weg2015.*
import com.jamesjmtaylor.weg2015.models.entities.Gun
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class EquipmentRepositoryTest  {
    private var app: App? = null
    private var repo: EquipmentRepository? = null
    @Before
    fun setUp() {
        val context = RuntimeEnvironment.application
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        app = RuntimeEnvironment.application as? App
        repo = EquipmentRepository()
        AppDatabase.setInstance(db)
    }
    @Test
    fun testDbGetAll() {
        val testGun = Gun(0,"BFG")
        AppDatabase.getInstance(app!!).GunDao().insertGuns(listOf(testGun))

        val liveData = repo?.getAll() //Start the asyncTask
        ShadowApplication.runBackgroundTasks()

        val actual = liveData?.value?.firstOrNull()?.name ?: NO_DATA
        val expected = testGun.name
        assertEquals(expected, actual)
    }

    @Test
    fun testNetworkGetAll() {
        val json = "{\n" +
                "       guns: [\n" +
                "           {\n" +
                "               name: \"2B14; 82mm\",\n" +
                "               groupIconUrl: \"group/enmortar\",\n" +
                "               individualIconUrl: \"individual/enmedmortar\",\n" +
                "               photoUrl: \"photo/2b14\",\n" +
                "               range: 13000,\n" +
                "               penetration: 20,\n" +
                "               altitude: 0,\n" +
                "               description: \"The 2B14 Podnos is a Soviet 82mm mortar.\",\n" +
                "               id: 1\n" +
                "           }\n" +
                "      ]\n" +
                "}"
        setOfflineWebMock(json)

        val liveData = repo?.getAll() //Start the asyncTask
        ShadowApplication.runBackgroundTasks()

        val actual = liveData?.value?.firstOrNull()?.name ?: NO_DATA
        val expected = "2B14; 82mm"
        assertEquals(expected, actual)
    }
}