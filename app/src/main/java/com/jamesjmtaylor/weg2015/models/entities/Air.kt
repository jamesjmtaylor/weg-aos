package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "air")
data class Air(override @PrimaryKey val id: Long = 0,
               override val name: String, val description: String? = null,
               val groupIconUrl: String? = null, val individualIcon: String? = null,
               override val photoUrl: String? = null,

               @Embedded(prefix = "gun") var gun: Gun? = null,
               @Embedded(prefix = "agm") var agm: Gun? = null,
               @Embedded(prefix = "aam") var aam: Gun? = null,
               @Embedded(prefix = "asm") var asm: Gun? = null,

               val speed: Int? = null, var auto: Int? = null, var ceiling: Int? = null,
               val weight: Int? = null): Equipment {
    @Ignore override val type = EquipmentType.AIR

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        val e = other as? Equipment
        return id == e?.id && e.type == EquipmentType.AIR
    }
    class AirList : ArrayList<Air>()//Used for GSON deserialization
}
fun parseAirResponseString(response: String): List<Air> {
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Air>>(response, Air.AirList::class.java)
    return guns
}