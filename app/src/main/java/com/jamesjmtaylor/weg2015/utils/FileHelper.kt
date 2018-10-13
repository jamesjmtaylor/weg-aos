package com.jamesjmtaylor.weg2015.utils

import android.content.Context
import android.graphics.Bitmap
import com.jamesjmtaylor.weg2015.App
import android.graphics.BitmapFactory
import android.util.Log
import com.jamesjmtaylor.weg2015.baseUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*

//fun loadImageFromStorage(imageName: String) : Bitmap? {
//    val f = openFile(imageName)
//    var fis : FileInputStream? = null
//    try {
//        fis = FileInputStream(f)
//        return BitmapFactory.decodeStream(fis)
//    } catch (e: FileNotFoundException) {
//        e.printStackTrace()
//    } finally {
//        try {fis?.close()}
//        catch (e: IOException) {e.printStackTrace() }
//    }
//    return null
//}
fun saveUrlToFile(imgUrl: String?) {
    imgUrl?.let { name ->
        val imgRequest = Request.Builder()
                .url(baseUrl + name)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build()
        val imgResponse = App.appWebClient.newCall(imgRequest).execute()
        if (imgResponse.isSuccessful) {
            val inputStream = imgResponse.body()?.byteStream()
            val bm = BitmapFactory.decodeStream(inputStream)
            bm.saveWithName(name)
        }
    }
}

fun Bitmap.saveWithName(name:String){
    val context = App.instance.applicationContext
    context.openFileOutput(name.removePathSeperators(), Context.MODE_PRIVATE).use {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

fun openFile(imageName: String?): File {
    val context = App.instance.applicationContext
    val files = context.fileList().joinToString(", ")
    Log.d("FILES",files) //For debugging purposes.

    val directory = context.filesDir
    val file = File(directory, imageName?.removePathSeperators())
    return file
}
private fun String.removePathSeperators():String{
    return this.replace("/","-")
}
