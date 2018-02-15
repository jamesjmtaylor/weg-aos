package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.AppDatabase
import com.jamesjmtaylor.weg2015.Models.DataState
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.Models.parseEquipmentResponseString
import com.jamesjmtaylor.weg2015.WebClient
import okhttp3.Request
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
    fun getGuns(): LiveData<List<Gun>> {
        refreshGuns()
        return db.GunDao().getAllGunsLiveData()
    }
    private fun refreshGuns() {
        //TODO: Implement condition on getting from network
        isLoading.postValue(true)
        thread {
            val request = Request.Builder()
                    .url("http://10.0.2.2:8080/findall/")
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build()
            try {
                val response = webservice.newCall(request).execute()
                val responseBody = response.body()?.string() ?: ""
                if (response.isSuccessful) {
                    val fetchedGuns = parseEquipmentResponseString(responseBody)
                    db.GunDao().insertGuns(fetchedGuns)
                } else {
                    var error = response.message()
                }
            } catch (e: Exception){
                Log.e(TAG,e.localizedMessage)
            }
            isLoading.postValue(false)
        }
    }
}


