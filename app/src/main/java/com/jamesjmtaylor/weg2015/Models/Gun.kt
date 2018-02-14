package com.jamesjmtaylor.weg2015.Models

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder

@Entity(tableName = "gun")
data class Gun(@ColumnInfo(name = "name") var name: String = "",
               @ColumnInfo(name = "description") var description: String = "",
               @ColumnInfo(name = "groupIconUrl") var groupIconUrl: String = "",
               @ColumnInfo(name = "individualIcon") var individualIcon: String = "",
               @ColumnInfo(name = "penetration") var penetration: Int = 0,
               @ColumnInfo(name = "photoUrl") var photoUrl: String = "",
               @ColumnInfo(name = "range") var range: Int = 0) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Gun).id
    }
}
fun parseEquipmentResponseString(response: String): List<Gun> {
    //Throws "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $"
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Gun>>(response, Gun::class.java)
    return guns
}