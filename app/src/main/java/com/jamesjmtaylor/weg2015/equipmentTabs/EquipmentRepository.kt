package com.jamesjmtaylor.weg2015.equipmentTabs


import android.content.Context
import android.graphics.Bitmap
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.getAll
import com.jamesjmtaylor.weg2015.models.CombinedLists
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea
import com.jamesjmtaylor.weg2015.models.parseEquipmentResponseString
import com.jamesjmtaylor.weg2015.utils.saveUrlToFile
import okhttp3.Request
import java.util.*


/**
 * Created by jtaylor on 2/13/18.
 */
class EquipmentRepository private constructor() {
    var gun: List<Gun>? = null
    var land: List<Land>? = null
    var sea: List<Sea>? = null
    var air: List<Air>? = null
    var equipment: List<Equipment>? = null


    fun getAll(): List<Equipment> {
        if (shouldFetch()) return getCombinedList()
        return equipment ?: emptyList()
    }

    private fun getCombinedList(): List<Equipment> {
        val request = Request.Builder()
                .url(getAll)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build()

        val db = App.appDatabase
        val response = App.appWebClient.newCall(request).execute()
        val responseBody = response.body()?.string() ?: ""
        if (response.isSuccessful) {
            saveFetchDate()
            val fetchedCombinedList = parseEquipmentResponseString(responseBody)

            //Doesn't come from API with type, assign here.
            fetchedCombinedList.guns.map { it.type = EquipmentType.GUN }
            fetchedCombinedList.land.map { it.type = EquipmentType.LAND }
            fetchedCombinedList.sea.map { it.type = EquipmentType.SEA }
            fetchedCombinedList.air.map { it.type = EquipmentType.AIR }

            saveImages(fetchedCombinedList)

            db.GunDao().insertGuns(fetchedCombinedList.guns)
            db.LandDao().insertLand(fetchedCombinedList.land)
            db.SeaDao().insertSea(fetchedCombinedList.sea)
            db.AirDao().insertAir(fetchedCombinedList.air)
            return fetchedCombinedList.getEquipment()
        }
        return emptyList<Equipment>()
    }

    private fun saveImages(fetchedCombinedLists: CombinedLists) {
        for (e in fetchedCombinedLists.getEquipment()) {
            when (e.type) {
                EquipmentType.GUN -> {
                    saveUrlToFile((e as Gun).individualIconUrl)
                    saveUrlToFile(e.groupIconUrl)
                }
                EquipmentType.LAND -> {
                    saveUrlToFile((e as Land).individualIconUrl)
                    saveUrlToFile(e.groupIconUrl)
                }
                EquipmentType.SEA -> {
                    saveUrlToFile((e as Sea).individualIconUrl)
                }
                EquipmentType.AIR -> {
                    saveUrlToFile((e as Air).individualIconUrl)
                    saveUrlToFile(e.groupIconUrl)
                }
                EquipmentType.ALL -> {
                }
            }
            saveUrlToFile(e.photoUrl, Bitmap.CompressFormat.JPEG)
        }
    }

    private fun shouldFetch(): Boolean {
        val app = App.INSTANCE
        val pref = app.getSharedPreferences(app.getString(R.string.bundle_id), Context.MODE_PRIVATE)
        val dateLastFetched = pref.getLong(DATE_FETCHED_KEY, 0)
        return true//dateLastFetched + (7 * 24 * 60 * 60 * 1000) < Date().time
    }

    private fun saveFetchDate() {
        val app = App.INSTANCE
        val pref = app.getSharedPreferences(app.getString(R.string.bundle_id), Context.MODE_PRIVATE)
        pref.edit().putLong(DATE_FETCHED_KEY, Date().time).apply()
    }

    companion object {
        const val DATE_FETCHED_KEY = "dateLastFetched"
        private var INSTANCE: EquipmentRepository? = null
        fun getInstance(): EquipmentRepository {
            if (INSTANCE == null) INSTANCE = EquipmentRepository()
            return INSTANCE as EquipmentRepository
        }

        fun setInstance(repo: EquipmentRepository) {
            INSTANCE = repo
        }
    }
}





