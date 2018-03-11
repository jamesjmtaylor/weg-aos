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
    val repo = EquipmentRepository()
    val equipment = MediatorLiveData<List<Equipment>>() //Mediator allows this class to pass the RoomLiveData from the repo class to the View
    val isLoading = MediatorLiveData<Boolean>()
    var filterResults = ArrayList<Equipment>()
    private var selectedType : EquipmentType = EquipmentType.LAND

    init {
        isLoading.addSource(repo.isLoading){isLoading.value=it}
        val source = getCurrentSource()
        if (source != null){
            equipment.addSource(source) {
                equipment.value = it
            }
        }
    }

    fun selectType(type:EquipmentType) {
        equipment.removeSource(getCurrentSource() ?: return)
        selectedType = type
        equipment.addSource(getCurrentSource() ?: return){
            equipment.value = it
            filterResults = it as ArrayList<Equipment>
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
    fun resetSearch() {

    }
}