package com.jamesjmtaylor.weg2015.Models

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "gun")
data class Gun(@ColumnInfo(name = "description") val description: String = "",
               @ColumnInfo(name = "groupIconUrl") var groupIconUrl: String = "",
               @ColumnInfo(name = "individualIcon") var individualIcon: String = "",
               @ColumnInfo(name = "name") var name: String = "",
               @ColumnInfo(name = "penetration") var penetration: Int = 0,
               @ColumnInfo(name = "photoUrl") var photoUrl: String = "",
               @ColumnInfo(name = "range") var range: Int = 0) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        return id == (other as Gun).id
    }
}