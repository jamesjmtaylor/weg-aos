package com.jamesjmtaylor.weg2015.Models

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder

@Entity(tableName = "gun")
data class Gun(@ColumnInfo(name = "name") val name: String = "",
               @ColumnInfo(name = "description") val description: String = "",
               @ColumnInfo(name = "groupIconUrl") val groupIconUrl: String = "",
               @ColumnInfo(name = "individualIcon") val individualIcon: String = "",
               @ColumnInfo(name = "penetration") val penetration: Int = 0,
               @ColumnInfo(name = "photoUrl") val photoUrl: String = "",
               @ColumnInfo(name = "range") val range: Int = 0) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Gun).id
    }
}
fun parseEquipmentResponseString(response: String): List<Gun> {
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Gun>>(response, Gun::class.java!!)
    return guns
}