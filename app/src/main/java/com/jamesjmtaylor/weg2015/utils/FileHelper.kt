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
import java.io.*

fun saveUrlToFile(imgUrl: String?) {
    val TAG = "SAVE"
        imgUrl?.let { name ->
            val imgRequest = Request.Builder()
                    .url(baseUrl + name)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build()
            val responseCallback = object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "OkHttp failed to obtain result", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val inputStream = response.body()?.byteStream()
                    val bm = BitmapFactory.decodeStream(inputStream) ?: return
                    bm.saveWithName(name)
                }

            }
            App.appWebClient.newCall(imgRequest).enqueue(responseCallback)
//            if (imgResponse.isSuccessful) {
//                val inputStream = imgResponse.body()?.byteStream()
//                val bm = BitmapFactory.decodeStream(inputStream)
//                bm.saveWithName(name)
//            }
    }
}

fun Bitmap.saveWithName(name:String){
    val context = App.instance.applicationContext
    context.openFileOutput(name.removePathSeperators(), Context.MODE_PRIVATE).use {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
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
        Log.d(TAG,e.localizedMessage)
        return null
    }

}
private fun String.removePathSeperators():String{
    return this.replace("/","-")
}
