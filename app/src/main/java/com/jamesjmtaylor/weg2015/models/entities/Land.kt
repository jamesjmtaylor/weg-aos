package com.jamesjmtaylor.weg2015.models.entities

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "land")
data class Land(override @PrimaryKey val id: Long = 0,
                override val name: String, val description: String? = null,
                val groupIconUrl: String? = null, val individualIconUrl: String? = null,
                override val photoUrl: String? = null,

                @Embedded(prefix = "primary") val primaryWeapon: Gun? = null,
                @Embedded(prefix = "secondary") val secondaryWeapon: Gun? = null,
                @Embedded(prefix = "atgm") val atgm: Gun? = null,

                val armor: Int? = null, val speed: Int? = null, val auto: Int? = null,
                val weight: Int? = null): Equipment, Parcelable {
    @Ignore override var type = EquipmentType.LAND

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        val e = other as? Equipment
        return id == e?.id && e.type == EquipmentType.LAND
    }
    class LandList : ArrayList<Land>()//Used for GSON deserialization

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(groupIconUrl)
        parcel.writeString(individualIconUrl)
        parcel.writeString(photoUrl)
        parcel.writeParcelable(primaryWeapon, flags)
        parcel.writeParcelable(secondaryWeapon, flags)
        parcel.writeParcelable(atgm, flags)
        parcel.writeValue(armor)
        parcel.writeValue(speed)
        parcel.writeValue(auto)
        parcel.writeValue(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Land> {
        override fun createFromParcel(parcel: Parcel): Land {
            return Land(parcel)
        }

        override fun newArray(size: Int): Array<Land?> {
            return arrayOfNulls(size)
        }
    }
}
fun parseLandResponseString(response: String): List<Land> {
    val gson = GsonBuilder().create()
    val land = gson.fromJson<List<Land>>(response, Land.LandList::class.java)
    return land
}