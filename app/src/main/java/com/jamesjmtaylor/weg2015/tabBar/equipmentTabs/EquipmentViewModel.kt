package com.jamesjmtaylor.weg2015.tabBar.equipmentTabs

import android.app.Application
import android.arch.lifecycle.*
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val equipment = MediatorLiveData<List<Equipment>>() //Mediator allows this class to pass the RoomLiveData from the repo class to the View
    val isLoading = MediatorLiveData<Boolean>()
    private var selectedType : EquipmentType = EquipmentType.LAND

    val repo = EquipmentRepository()
    fun initData() {
        isLoading.removeSource(repo.isLoading)
        isLoading.addSource(repo.isLoading){isLoading.value=it}
        equipment.addSource(getCurrentSource() ?: return) {
            equipment.value = it
        }
    }
    fun selectType(type:EquipmentType) {
        equipment.removeSource(getCurrentSource() ?: return)
        selectedType = type
        equipment.addSource(getCurrentSource() ?: return){
            equipment.value = it
        }
    }
    fun getCurrentSource(): LiveData<List<Equipment>>? {
        var data : LiveData<*>
        when (selectedType){
            EquipmentType.LAND -> data = repo.getLand()
            EquipmentType.SEA -> data = repo.getSea()
            EquipmentType.AIR -> data = repo.getAir()
            EquipmentType.GUN -> data = repo.getGun()
        }
        return data as? LiveData<List<Equipment>>
    }
}