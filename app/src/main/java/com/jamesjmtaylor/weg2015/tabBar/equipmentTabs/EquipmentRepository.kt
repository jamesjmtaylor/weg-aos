package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.arch.lifecycle.LiveData
import android.os.AsyncTask.execute
import android.util.Log
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.AppDatabase
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.Models.parseEquipmentResponseString
import com.jamesjmtaylor.weg2015.WebClient
import okhttp3.Request
import java.util.concurrent.Executor
import kotlin.concurrent.thread


/**
 * Created by jtaylor on 2/13/18.
 */
class EquipmentRepository {
    private val TAG = "EquipmentRepo"
    private var FRESH_TIMEOUT = 1000
    private val webservice = WebClient.getInstance()
    private val db = AppDatabase.getInstance(App.instance)
    fun getGuns(): LiveData<List<Gun>> {
        refreshGuns()
        // return a LiveData directly from the database.
        return db.GunDao().getAllGuns()
    }
    private fun refreshGuns() {
        thread {
            //            val userExists = db.GunDao().hasUser(FRESH_TIMEOUT)
//            if (!userExists) {
            val request = Request.Builder()
                    .url("http://10.0.2.2:8080/findall")
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

        }
    }
}

