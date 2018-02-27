package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.app.Application
import android.arch.lifecycle.*
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Sea

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val equipment = MediatorLiveData<List<Sea>>() //Mediator allows this class to pass the RoomLiveData from the repo class to the View
    val isLoading = MediatorLiveData<Boolean>()

    val repo = EquipmentRepository()
    fun initData() {
        if (isLoading.value == null){//Hasn't had a source added yet
            isLoading.addSource(repo.isLoading){
                isLoading.value = it
            }
        }
        equipment.addSource(repo.getSea()) {
            equipment.value = it
        }
    }
}