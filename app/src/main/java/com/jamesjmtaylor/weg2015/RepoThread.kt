package com.jamesjmtaylor.weg2015

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jamesjmtaylor.weg2015.equipmentTabs.EquipmentRepository
import com.jamesjmtaylor.weg2015.models.Equipment

class RepoThread(repo: EquipmentRepository, equipmentLiveData: LiveData<List<Equipment>>?, isLoading: LiveData<Boolean>?) : Thread() {
    val repo = repo
    var equipmentLiveData = equipmentLiveData as MutableLiveData<List<Equipment>>?
    var isLoading: MutableLiveData<Boolean>? = isLoading as MutableLiveData<Boolean>?

    init {
        isLoading ?: MutableLiveData<Boolean>()
        equipmentLiveData ?: MutableLiveData<List<Equipment>>()
    }

    override fun run() {
        isLoading?.postValue(false)
        equipmentLiveData?.postValue(repo.getAll())
    }
}