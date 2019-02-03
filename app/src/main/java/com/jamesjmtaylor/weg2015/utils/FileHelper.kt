package com.jamesjmtaylor.weg2015.utils

import android.content.Context
import android.graphics.Bitmap
import com.jamesjmtaylor.weg2015.App
import android.graphics.BitmapFactory
import android.util.Log
import com.jamesjmtaylor.weg2015.baseUrl
import okhttp3.Call
import okhttp3.Callback

import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.*

fun saveUrlToFile(imgUrl: String?, format: Bitmap.CompressFormat? = Bitmap.CompressFormat.PNG) {
    imgUrl?.let { name ->
        val imgRequest = Request.Builder()
                .url(baseUrl + name)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build()
        val call = App.appWebClient.newCall(imgRequest)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, throwable: IOException) {
                Timber.e(throwable, "OkHttp failed to obtain result")
            }
            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()?.byteStream()
                val bm = BitmapFactory.decodeStream(inputStream) ?: return
                val context = App.instance.applicationContext
                context.openFileOutput(name.removePathSeperators(), Context.MODE_PRIVATE).use {
                    bm.compress(format, 100, it)
                }
            }
        })
    }
}


fun openFile(imageName: String?): File? {
    val TAG = "FILES"
    val context = App.instance.applicationContext
    try {
        val directory = context.filesDir
        val file = File(directory, imageName?.removePathSeperators())
        return file
    } catch (e: Exception){
        Timber.e(e, "Could not open file")
        return null
    }

}
private fun String.removePathSeperators():String{
    return this.replace("/","-")
}
