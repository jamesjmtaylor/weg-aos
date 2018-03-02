package com.jamesjmtaylor.weg2015.models

import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea

/**
 * Created by jtaylor on 3/1/18.
 */
interface Equipment {
    val id : Long
    val name : String
    val photoUrl: String?
    val type : EquipmentType
}

enum class EquipmentType {
    LAND, SEA, AIR, GUN
}

class CombinedList(val guns: List<Gun>,
                   val land: List<Land>,
                   val sea: List<Sea>,
                   val air: List<Air>) {
    fun getEquipment():List<Equipment>{
        var equipmentList = ArrayList<Equipment>()
        equipmentList.addAll(guns)
        equipmentList.addAll(land)
        equipmentList.addAll(sea)
        equipmentList.addAll(air)
        return equipmentList
    }
}
fun parseEquipmentResponseString(response: String): CombinedList {
    val gson = GsonBuilder().create()
    val combinedList = gson.fromJson<CombinedList>(response, CombinedList::class.java)
    return combinedList
}