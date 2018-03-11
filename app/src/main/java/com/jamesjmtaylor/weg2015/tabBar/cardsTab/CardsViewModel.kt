package com.jamesjmtaylor.weg2015.tabBar.cardsTab

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 3/11/18.
 */
class CardsViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    val equipment = MediatorLiveData<List<Equipment>>()
    private var selectedType : EquipmentType = EquipmentType.LAND
}