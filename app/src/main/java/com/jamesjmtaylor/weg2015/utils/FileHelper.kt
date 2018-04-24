package com.jamesjmtaylor.weg2015.utils

import android.graphics.Bitmap
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import com.jamesjmtaylor.weg2015.App
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.*


fun saveToInternalStorage(imageName: String, bitmapImage: Bitmap) {
    val file = getFile(imageName)
    var fos : FileOutputStream? = null
    try {
        fos = FileOutputStream(file)// Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {fos?.close()}
        catch (e: IOException) {e.printStackTrace() }
    }
    //return directory.absolutePath
}

fun loadImageFromStorage(imageName: String, view: ImageView) {
    val f = getFile(imageName)
    var fis : FileInputStream? = null
    try {
        fis = FileInputStream(f)
        val b = BitmapFactory.decodeStream(fis)
        view.setImageBitmap(b)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } finally {
        try {fis?.close()}
        catch (e: IOException) {e.printStackTrace() }
    }
}

private fun getFile(imageName: String): File {
    val cw = ContextWrapper(App.instance.baseContext)
    val directory = cw.getDir("imageDir", MODE_PRIVATE)// path to /data/data/yourapp/app_data/imageDir
    val mypath = File(directory, "${imageName}.jpg")// Create imageDir
    return mypath
}
