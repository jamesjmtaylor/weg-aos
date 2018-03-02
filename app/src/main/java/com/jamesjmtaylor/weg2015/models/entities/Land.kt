package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.*
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "land")
data class Land(override @PrimaryKey val id: Long = 0,
                override val name: String, val description: String? = null,
                val groupIconUrl: String? = null, val individualIcon: String? = null,
                override val photoUrl: String? = null,

                @Embedded(prefix = "primary") val primaryWeapon: Gun? = null,
                @Embedded(prefix = "secondary") val secondaryWeapon: Gun? = null,
                @Embedded(prefix = "atgm") val atgm: Gun? = null,

                val armor: Int? = null, val speed: Int? = null, val auto: Int? = null,
                val weight: Int? = null): Equipment {
    @Ignore override val type = EquipmentType.LAND

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        val e = other as? Equipment
        return id == e?.id && e.type == EquipmentType.LAND
    }
    class LandList : ArrayList<Land>()//Used for GSON deserialization
}
fun parseLandResponseString(response: String): List<Land> {
    val gson = GsonBuilder().create()
    val land = gson.fromJson<List<Land>>(response, Land.LandList::class.java)
    return land
}