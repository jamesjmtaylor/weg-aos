package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "air")
data class Air(var name: String? = null, var description: String? = null,
               var groupIconUrl: String? = null, var individualIcon: String? = null,
               var photoUrl: String? = null,

               @Embedded(prefix = "gun") var gun: Gun? = null,
               @Embedded(prefix = "agm") var agm: Gun? = null,
               @Embedded(prefix = "aam") var aam: Gun? = null,
               @Embedded(prefix = "asm") var asm: Gun? = null,

               var speed: Int? = null, var auto: Int? = null, var ceiling: Int? = null,
               var weight: Int? = null) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Air).id
    }
    class AirList : ArrayList<Air>()//Used for GSON deserialization
}
fun parseAirResponseString(response: String): List<Air> {
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Air>>(response, Air.AirList::class.java)
    return guns
}