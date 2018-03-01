package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "sea")
data class Sea(override @PrimaryKey val id: Long = 0,
               override val name: String, val description: String? = null,
               val individualIcon: String? = null, override val photoUrl: String? = null,

               @Embedded(prefix = "gun") val gun: Gun? = null,
               @Embedded(prefix = "sam") val sam: Gun? = null,
               @Embedded(prefix = "asm") val asm: Gun? = null,
               @Embedded(prefix = "torpedo") val torpedo: Gun? = null,

               val transports: String? = null, val qty: Int? = null, val dive: Int? = null,
               val speed: Int? = null, val auto: Int? = null, val tonnage: Int? = null):Equipment{
    override val type = EquipmentType.SEA

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Sea).id
    }
    class SeaList : ArrayList<Sea>()//Used for GSON deserialization
}
fun parseSeaResponseString(response: String): List<Sea> {
    val gson = GsonBuilder().create()
    val sea = gson.fromJson<List<Sea>>(response, Sea.SeaList::class.java)
    return sea
}