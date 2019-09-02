package com.jamesjmtaylor.weg2015.models

import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.entities.Air
import com.jamesjmtaylor.weg2015.models.entities.Gun
import com.jamesjmtaylor.weg2015.models.entities.Land
import com.jamesjmtaylor.weg2015.models.entities.Sea

/**
 * Created by jtaylor on 3/1/18.
 */
interface Equipment {
    val id: Long
    val name: String
    val photoUrl: String?
    val type: EquipmentType
}

enum class EquipmentType {
    LAND, SEA, AIR, GUN, ALL
}

class CombinedLists(val guns: List<Gun>,
                    val land: List<Land>,
                    val sea: List<Sea>,
                    val air: List<Air>) {
    fun getEquipment(): List<Equipment> {
        val equipmentList = ArrayList<Equipment>()
        equipmentList.addAll(guns)
        equipmentList.addAll(land)
        equipmentList.addAll(sea)
        equipmentList.addAll(air)
        return equipmentList
    }
}

fun parseEquipmentResponseString(response: String): CombinedLists {
    val gson = GsonBuilder().create()
    val combinedList = gson.fromJson<CombinedLists>(response, CombinedLists::class.java)
    return combinedList
}

fun parcelizeEquipment(equipment: Equipment, intent: Intent): Intent {
    when (equipment.type) {
        EquipmentType.LAND -> intent.putExtra("equipment", equipment as? Land)
        EquipmentType.SEA -> intent.putExtra("equipment", equipment as? Sea)
        EquipmentType.AIR -> intent.putExtra("equipment", equipment as? Air)
        EquipmentType.GUN -> intent.putExtra("equipment", equipment as? Gun)
        EquipmentType.ALL -> {
        }
    }
    intent.putExtra("type", equipment.type)
    return intent
}

fun deParcelizeEquipment(bundle: Bundle?): Equipment? {
    val type = bundle?.get("type")
    var equipment: Equipment? = null
    when (type) {
        EquipmentType.LAND -> equipment = bundle.getParcelable<Land>("equipment")
        EquipmentType.SEA -> equipment = bundle.getParcelable<Sea>("equipment")
        EquipmentType.AIR -> equipment = bundle.getParcelable<Air>("equipment")
        EquipmentType.GUN -> equipment = bundle.getParcelable<Gun>("equipment")
    }
    return equipment
}