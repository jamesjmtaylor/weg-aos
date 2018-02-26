package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "land")
data class Land(var name: String? = null, var description: String? = null,
                var groupIconUrl: String? = null, var individualIcon: String? = null,
                var photoUrl: String? = null,

                @Embedded(prefix = "primary") var primaryWeapon: Gun? = null,
                @Embedded(prefix = "secondary") var secondaryWeapon: Gun? = null,
                @Embedded(prefix = "atgm") var atgm: Gun? = null,

                var armor: Int? = null, var speed: Int? = null, var auto: Int? = null,
                var weight: Int? = null) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Land).id
    }
    class LandList : ArrayList<Land>()//Used for GSON deserialization
}
fun parseLandResponseString(response: String): List<Land> {
    val gson = GsonBuilder().create()
    val land = gson.fromJson<List<Land>>(response, Land.LandList::class.java)
    return land
}