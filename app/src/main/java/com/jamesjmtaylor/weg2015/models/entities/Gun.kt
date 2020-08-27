package com.jamesjmtaylor.weg2015.models.entities

/**
 * Created by jtaylor on 2/10/18.
 */
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

@Entity(tableName = "gun")
data class Gun(override @PrimaryKey val id: Long = 0,
               override val name: String,
               val description: String? = null,
               val groupIconUrl: String? = null,
               val individualIconUrl: String? = null,
               val penetration: Int? = null,
               val altitude: Int? = null,
               override val photoUrl: String? = null,
               val range: Int? = null) : Equipment, Parcelable {
    @Ignore
    override var type = EquipmentType.GUN

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(groupIconUrl)
        parcel.writeString(individualIconUrl)
        parcel.writeValue(penetration)
        parcel.writeValue(altitude)
        parcel.writeString(photoUrl)
        parcel.writeValue(range)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gun> {
        override fun createFromParcel(parcel: Parcel): Gun {
            return Gun(parcel)
        }

        override fun newArray(size: Int): Array<Gun?> {
            return arrayOfNulls(size)
        }
    }

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