package com.jamesjmtaylor.weg2015.equipmentTabs


import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.*
import com.jamesjmtaylor.weg2015.models.parseEquipmentResponseString
import okhttp3.Request

import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread

import com.jamesjmtaylor.weg2015.*
import com.jamesjmtaylor.weg2015.models.CombinedList
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.utils.saveUrlToFile
import com.jamesjmtaylor.weg2015.utils.saveWithName
import okhttp3.Response
import kotlin.collections.ArrayList


/**
 * Created by jtaylor on 2/13/18.
 */
class EquipmentRepository {
    var isLoading = MutableLiveData<Boolean>() //Mutable allows this class to post changes to observing views
    var gun : List<Gun>? = null
    var land : List<Land>? = null
    var sea : List<Sea>? = null
    var air : List<Air>? = null

    fun getGun(): LiveData<List<Equipment>> {
        return refreshCombined(EquipmentType.GUN) ?: getInMemoryAsLiveData(EquipmentType.GUN)
    }
    fun getLand(): LiveData<List<Equipment>> {
        return refreshCombined(EquipmentType.LAND) ?: getInMemoryAsLiveData(EquipmentType.LAND)
    }
    fun getSea(): LiveData<List<Equipment>> {
        return refreshCombined(EquipmentType.SEA) ?: getInMemoryAsLiveData(EquipmentType.SEA)
    }
    fun getAir(): LiveData<List<Equipment>> {
        return refreshCombined(EquipmentType.AIR) ?: getInMemoryAsLiveData(EquipmentType.AIR)
    }
    fun getAll(): LiveData<List<Equipment>> {
        return refreshCombined(EquipmentType.ALL) ?: getInMemoryAsLiveData(EquipmentType.ALL)
    }
    private fun refreshCombined(type: EquipmentType):MutableLiveData<List<Equipment>>? {
        if (shouldFetch()){
            val mutable = MutableLiveData<List<Equipment>>()
            isLoading.postValue(true)
            GetEquipmentTask(gun,land,sea,air,mutable,isLoading,type).execute()
            return mutable
        }
        return null
    }

    fun getInMemoryAsLiveData(type: EquipmentType):LiveData<List<Equipment>>{
        val mutable = MutableLiveData<List<Equipment>>()
        GetInMemoryAsLiveDataTask(gun,land,sea,air,mutable,type).execute()
        return mutable
    }
    private class GetInMemoryAsLiveDataTask(var gun: List<Gun>?,
                                            var land: List<Land>?,
                                            var sea: List<Sea>?,
                                            var air: List<Air>?,
                                            var mutable: MutableLiveData<List<Equipment>>,
                                            var type: EquipmentType) : AsyncTask<Void, Void, Void?>() { @SuppressLint("LogNotTimber")
    override fun doInBackground(vararg voids: Void): Void? {
        val db = App.appDatabase
        when (type){
            EquipmentType.GUN -> mutable.postValue(gun ?: db.GunDao().getAllGuns())
            EquipmentType.LAND -> postLandAndGunsLiveData(gun,land,mutable)
            EquipmentType.SEA -> mutable.postValue(sea ?: db.SeaDao().getAllSea())
            EquipmentType.AIR -> mutable.postValue(air ?: db.AirDao().getAllAir())
            EquipmentType.ALL -> postAll(gun,land,sea,air,mutable)
        }
        return null
    }
    }
    class GetEquipmentTask(var gun: List<Gun>?,
                           var land: List<Land>?,
                           var sea: List<Sea>?,
                           var air: List<Air>?,
                           var mutable: MutableLiveData<List<Equipment>>,
                           var isLoading: MutableLiveData<Boolean>,
                           var type: EquipmentType)
        : AsyncTask<Void, Void, Void?>() { @SuppressLint("LogNotTimber")
    override fun doInBackground(vararg voids: Void): Void? {
        val request = Request.Builder()
                .url(getAll)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build()
        try {
            val db = App.appDatabase
            val response = App.appWebClient.newCall(request).execute()
            val responseBody = response.body()?.string() ?: ""
            if (response.isSuccessful) {
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

                saveFetchDate()

                when (type){
                    EquipmentType.GUN -> mutable.postValue(gun)
                    EquipmentType.LAND -> postLandAndGunsLiveData(gun,land,mutable)
                    EquipmentType.SEA -> mutable.postValue(sea)
                    EquipmentType.AIR -> mutable.postValue(air)
                    EquipmentType.ALL -> postAll(gun,land,sea,air,mutable)
                }
            } else {
                val code = response.code().toString()
                val error = response.message() ?: "No error provided"
                Log.e(TAG, "$code: $error")
            }
        } catch (e: Exception){
            Log.e(TAG,e.localizedMessage)
        }
        isLoading.postValue(false)
        return null
    }

        private fun saveImages(fetchedCombinedList: CombinedList) {
            for (e in fetchedCombinedList.getEquipment()) {
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
                saveUrlToFile(e.photoUrl)
            }
        }
    }
}
private val DATE_FETCHED_KEY = "dateLastFetched"
private fun shouldFetch(): Boolean {
    val app = App.instance
    val pref = app.getSharedPreferences(app.getString(R.string.bundle_id), Context.MODE_PRIVATE)
    val dateLastFetched = pref.getLong(DATE_FETCHED_KEY, 0)
    return dateLastFetched + (7 * 24 * 60 * 60 * 1000) < Date().time
}
private fun saveFetchDate(){
    val app = App.instance
    val pref = app.getSharedPreferences(app.getString(R.string.bundle_id), Context.MODE_PRIVATE)
    pref.edit().putLong(DATE_FETCHED_KEY,Date().time).apply()
}
private fun postLandAndGunsLiveData(gun: List<Gun>?,
                                    land: List<Land>?,
                                    mutable: MutableLiveData<List<Equipment>>){
    val db = App.appDatabase
    val landAndGuns = ArrayList<Equipment>()
    landAndGuns.addAll(gun ?: db.GunDao().getAllGuns())
    landAndGuns.addAll(land ?: db.LandDao().getAllLand())
    val nonDisplayableFilteredOut = landAndGuns.filter { !it.photoUrl.isNullOrBlank() }
    val sorted = nonDisplayableFilteredOut.sortedBy { it.name }
    mutable.postValue(sorted)
}
private fun postAll(gun: List<Gun>?,
                    land: List<Land>?,
                    sea: List<Sea>?,
                    air: List<Air>?,
                    mutable: MutableLiveData<List<Equipment>>) {
    val db = App.appDatabase
    val guns : List<Equipment> = gun ?: db.GunDao().getAllGuns()
    val nonDisplayableFilteredOut = guns.filter { g -> !g.photoUrl.isNullOrBlank() }
    (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(land ?: db.LandDao().getAllLand())
    (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(sea ?: db.SeaDao().getAllSea())
    (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(air ?: db.AirDao().getAllAir())

    val sorted = nonDisplayableFilteredOut.sortedBy { it.name }
    mutable.postValue(sorted)
}


