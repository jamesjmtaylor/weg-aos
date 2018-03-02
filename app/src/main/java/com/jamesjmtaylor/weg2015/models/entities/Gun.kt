package com.jamesjmtaylor.weg2015.models.entities

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

@Entity(tableName = "gun")
data class Gun(override @PrimaryKey val id: Long = 0,
               override val name: String,
               val description: String? = null,
               val groupIconUrl: String? = null,
               val individualIcon: String? = null,
               val penetration: Int? = null,
               override val photoUrl: String? = null,
               val range: Int? = null): Equipment {
    @Ignore override val type = EquipmentType.GUN

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        val e = other as? Equipment
        return id == e?.id && e.type == EquipmentType.GUN
    }
    class GunList : ArrayList<Gun>()//Used for GSON deserialization
}
fun parseGunResponseString(response: String): List<Gun> {
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Gun>>(response, Gun.GunList::class.java)
    return guns
}