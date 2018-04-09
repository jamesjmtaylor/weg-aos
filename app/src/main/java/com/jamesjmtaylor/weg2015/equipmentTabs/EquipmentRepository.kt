package com.jamesjmtaylor.weg2015.equipmentTabs


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.*
import com.jamesjmtaylor.weg2015.models.parseEquipmentResponseString
import okhttp3.Request

import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread

import com.jamesjmtaylor.weg2015.*
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import kotlin.collections.ArrayList


/**
 * Created by jtaylor on 2/13/18.
 */
class EquipmentRepository {
    private val TAG = "EquipmentRepo"

    private val webservice = WebClient.getInstance()
    private val db = AppDatabase.getInstance(App.instance)
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


    private val DATE_FETCHED_KEY = "dateLastFetched"
    private fun refreshCombined(type: EquipmentType):MutableLiveData<List<Equipment>>? {
        if (shouldFetch()){
            val mutable = MutableLiveData<List<Equipment>>()
            isLoading.postValue(true)
            thread {
                val request = Request.Builder()
                        .url(getAll)
                        .get()
                        .addHeader("Cache-Control", "no-cache")
                        .build()
                try {
                    val response = webservice.newCall(request).execute()
                    val responseBody = response.body()?.string() ?: ""
                    if (response.isSuccessful) {
                        val fetchedCombinedList = parseEquipmentResponseString(responseBody)
                        gun = fetchedCombinedList.guns
                        land = fetchedCombinedList.land
                        sea = fetchedCombinedList.sea
                        air = fetchedCombinedList.air
                        //Doesn't come from API with type, assign here.
                        gun?.map { it.type = EquipmentType.GUN }
                        land?.map { it.type = EquipmentType.LAND }
                        sea?.map { it.type = EquipmentType.SEA }
                        air?.map { it.type = EquipmentType.AIR }
                        //Force unwrap safe because they were just assigned AND try/catch block
                        db.LandDao().insertLand(land!!)
                        db.GunDao().insertGuns(gun!!)
                        db.SeaDao().insertSea(sea!!)
                        db.AirDao().insertAir(air!!)
                        saveFetchDate()

                        when (type){
                            EquipmentType.GUN -> mutable.postValue(gun)
                            EquipmentType.LAND -> postLandAndGunsLiveData(mutable)
                            EquipmentType.SEA -> mutable.postValue(sea)
                            EquipmentType.AIR -> mutable.postValue(air)
                            EquipmentType.ALL -> postAll(mutable)
                        }
                    } else {
                        Log.e(TAG,response.message())
                    }
                } catch (e: Exception){
                    Log.e(TAG,e.localizedMessage)
                }
                try {sleep(2000)} catch (e: Exception){}//So loading animation has a chance to show
                isLoading.postValue(false)

            }
            return mutable
        }
        return null
    }

    fun getInMemoryAsLiveData(type: EquipmentType):LiveData<List<Equipment>>{
        val mutable = MutableLiveData<List<Equipment>>()
        thread {
            when (type){
                EquipmentType.GUN -> mutable.postValue(gun ?: db.GunDao().getAllGuns())
                EquipmentType.LAND -> postLandAndGunsLiveData(mutable)
                EquipmentType.SEA -> mutable.postValue(sea ?: db.SeaDao().getAllSea())
                EquipmentType.AIR -> mutable.postValue(air ?: db.AirDao().getAllAir())
                EquipmentType.ALL -> postAll(mutable)
            }
        }
        return mutable
    }
    private fun postLandAndGunsLiveData(mutable: MutableLiveData<List<Equipment>>){
        thread {
            val guns: List<Equipment> = this.gun ?: db.GunDao().getAllGuns()
            val land: List<Equipment> = this.land ?: db.LandDao().getAllLand()
            val landAndGuns = ArrayList<Equipment>()
            landAndGuns.addAll(guns)
            landAndGuns.addAll(land)
            val nonDisplayableFilteredOut = landAndGuns.filter { !it.photoUrl.isNullOrBlank() }
            val sorted = nonDisplayableFilteredOut.sortedBy { it.name }
            mutable.postValue(sorted)
        }
    }
    private fun postAll(mutable: MutableLiveData<List<Equipment>>) {
        thread {
            val guns : List<Equipment> = this.gun ?: db.GunDao().getAllGuns()
            val nonDisplayableFilteredOut = guns.filter { gun -> !gun.photoUrl.isNullOrBlank() }
            val land : List<Equipment> = this.land ?: db.LandDao().getAllLand()
            val sea : List<Equipment> = this.sea ?: db.SeaDao().getAllSea()
            val air : List<Equipment> = this.air ?: db.AirDao().getAllAir()
            (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(land)
            (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(sea)
            (nonDisplayableFilteredOut as? ArrayList<Equipment>)?.addAll(air)

            val sorted = nonDisplayableFilteredOut.sortedBy { it.name }
            mutable.postValue(sorted)
        }
    }

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
}


