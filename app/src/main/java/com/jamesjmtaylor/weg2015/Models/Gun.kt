package com.jamesjmtaylor.weg2015.Models

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

@Entity(tableName = "gun")
data class Gun(@ColumnInfo(name = "name") var name: String? = null,
               @ColumnInfo(name = "description") var description: String? = null,
               @ColumnInfo(name = "groupIconUrl") var groupIconUrl: String? = null,
               @ColumnInfo(name = "individualIcon") var individualIcon: String? = null,
               @ColumnInfo(name = "penetration") var penetration: Int? = null,
               @ColumnInfo(name = "photoUrl") var photoUrl: String? = null,
               @ColumnInfo(name = "range") var range: Int? = null) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Gun).id
    }
    class GunList : ArrayList<Gun>()//Used for GSON deserialization
}
fun parseEquipmentResponseString(response: String): List<Gun> {
    //Throws "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $"
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Gun>>(response, Gun.GunList::class.java)
    return guns
}