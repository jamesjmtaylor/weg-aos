package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "sea")
data class Sea(@PrimaryKey var id: Long = 0,
               var name: String? = null, var description: String? = null,
               var individualIcon: String? = null, var photoUrl: String? = null,

               @Embedded(prefix = "gun") var gun: Gun? = null,
               @Embedded(prefix = "sam") var sam: Gun? = null,
               @Embedded(prefix = "asm") var asm: Gun? = null,
               @Embedded(prefix = "torpedo") var torpedo: Gun? = null,

               var transports: String? = null, var qty: Int? = null, var dive: Int? = null,
               var speed: Int? = null, var auto: Int? = null, var tonnage: Int? = null){
//    @ColumnInfo(name = "id")
//    @PrimaryKey(autoGenerate = true) var id: Long = 0

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