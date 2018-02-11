package com.jamesjmtaylor.weg2015.tabBar

import android.app.Application
import android.arch.lifecycle.*
import android.os.AsyncTask
import com.jamesjmtaylor.weg2015.App
import com.jamesjmtaylor.weg2015.Models.Gun
import java.io.File
import kotlin.concurrent.thread

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    var guns = MutableLiveData<List<Gun>>()

    fun initEquipment() {

        var gunList = ArrayList<Gun>()
        for (i in 0..100){
            gunList.add(Gun("Gun "+i.toString()))
        }
        guns.postValue(gunList)

    }

}