package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.AppDatabase
import com.jamesjmtaylor.weg2015.R
import com.jamesjmtaylor.weg2015.WebClient
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.*
import com.jamesjmtaylor.weg2015.models.parseEquipmentResponseString
import okhttp3.Request
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread


/**
 * Created by jtaylor on 2/13/18.
 */
class EquipmentRepository {
    private val TAG = "EquipmentRepo"

    private var FRESH_TIMEOUT = 1000
    private val webservice = WebClient.getInstance()
    private val db = AppDatabase.getInstance(App.instance)
    var isLoading = MutableLiveData<Boolean>() //Mutable allows this class to post changes to observing views
    //TODO: Eventually have an in-memory store as well for 3-tiered fetch
    fun getGun(): LiveData<List<Gun>> {
        refreshCombined()
        return db.GunDao().getAllGunsLiveData()
    }
    fun getLand(): LiveData<List<Land>> {
        refreshCombined()
        return db.LandDao().getAllLandLiveData()
    }
    fun getSea(): LiveData<List<Sea>> {
        refreshCombined()
        return db.SeaDao().getAllSeaLiveData();
    }
    fun getAir(): LiveData<List<Air>> {
        refreshCombined()
        return db.AirDao().getAllAirLiveData();
    }

    private val DATE_FETCHED_KEY = "dateLastFetched"
    private fun refreshCombined() {
        if (shouldFetch()){
            isLoading.postValue(true)
            thread {
                val request = Request.Builder()
                        .url("http://10.0.2.2:8080/getAllCombined/")
                        .get()
                        .addHeader("Cache-Control", "no-cache")
                        .build()
                try {
                    val response = webservice.newCall(request).execute()
                    val responseBody = response.body()?.string() ?: ""
                    if (response.isSuccessful) {
                        val fetchedCombinedList = parseEquipmentResponseString(responseBody)
                        db.LandDao().insertLand(fetchedCombinedList.land)
                        db.GunDao().insertGuns(fetchedCombinedList.guns)
                        db.SeaDao().insertSea(fetchedCombinedList.sea)
                        db.AirDao().insertAir(fetchedCombinedList.air)
                        saveFetchDate()
                    } else {
                        var error = response.message()
                    }
                } catch (e: Exception){
                    Log.e(TAG,e.localizedMessage)
                }
                try {sleep(2000)} catch (e: Exception){}//So loading animation has a chance to show
                isLoading.postValue(false)
            }}
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


