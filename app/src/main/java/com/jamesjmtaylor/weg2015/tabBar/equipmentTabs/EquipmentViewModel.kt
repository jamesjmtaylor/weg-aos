package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.app.Application
import android.arch.lifecycle.*
import com.jamesjmtaylor.weg2015.Models.Gun
import kotlin.concurrent.thread

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    var equipment = MutableLiveData<List<Gun>>()
    var isLoading = MutableLiveData<Boolean>()

    fun initData() {
        isLoading.setValue(true)
        thread { Thread.sleep(5000) //Simulates async network call
            var gunList = ArrayList<Gun>()
            for (i in 0..100){
                gunList.add(Gun("Gun "+i.toString()))
            }
            equipment.postValue(gunList)
            isLoading.postValue(false)
        }
    }
}