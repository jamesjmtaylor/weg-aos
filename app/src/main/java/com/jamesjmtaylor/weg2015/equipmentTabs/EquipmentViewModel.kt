package com.jamesjmtaylor.weg2015.equipmentTabs

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/10/18.
 */
class EquipmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val repo = EquipmentRepository.getInstance()
    val equipment = MutableLiveData<List<Equipment>>()
    val isLoading = MutableLiveData<Boolean>()
    var filterResults: List<Equipment>? = ArrayList<Equipment>()
    private var selectedType: EquipmentType = EquipmentType.LAND

    init {
//        repo.getAll() //Crashes app
    }

    fun selectType(type: EquipmentType) {
        selectedType = type
        filterResults = equipment.value?.filter { it.type == type }
    }
}