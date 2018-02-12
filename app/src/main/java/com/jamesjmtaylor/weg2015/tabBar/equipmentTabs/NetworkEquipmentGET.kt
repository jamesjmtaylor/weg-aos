package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.os.AsyncTask
import com.jamesjmtaylor.weg2015.Models.Gun
import com.jamesjmtaylor.weg2015.Models.parseEquipmentResponseString
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by jtaylor on 2/11/18.
 */

class NetworkEquipmentGET(val vm : EquipmentViewModel) : AsyncTask<String, Int, List<Gun>?>() {
    var error : String? = null
    override fun doInBackground(vararg p0: String?): List<Gun>? {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("http://localhost:8080/findall")
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            return parseEquipmentResponseString(response.message())
        }
        error = response.message()
        return null
    }
    override fun onPostExecute(guns: List<Gun>?) {
        if (error != null){

        } else {
            vm.equipment.setValue(guns)
        }
    }
}
