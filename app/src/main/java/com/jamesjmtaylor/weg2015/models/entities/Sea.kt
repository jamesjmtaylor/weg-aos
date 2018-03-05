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
@Entity(tableName = "sea")
data class Sea(override @PrimaryKey val id: Long = 0,
               override val name: String, val description: String? = null,
               val individualIcon: String? = null, override val photoUrl: String? = null,

               @Embedded(prefix = "gun") val gun: Gun? = null,
               @Embedded(prefix = "sam") val sam: Gun? = null,
               @Embedded(prefix = "asm") val asm: Gun? = null,
               @Embedded(prefix = "torpedo") val torpedo: Gun? = null,

               val transports: String? = null, val qty: Int? = null, val dive: Int? = null,
               val speed: Int? = null, val auto: Int? = null, val tonnage: Int? = null):Equipment, Parcelable {
    @Ignore override val type = EquipmentType.SEA

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readParcelable(Gun::class.java.classLoader),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun equals(other: Any?): Boolean { //needed for DiffUtil
        val e = other as? Equipment
        return id == e?.id && e.type == EquipmentType.SEA
    }
    class SeaList : ArrayList<Sea>()//Used for GSON deserialization

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(individualIcon)
        parcel.writeString(photoUrl)
        parcel.writeParcelable(gun, flags)
        parcel.writeParcelable(sam, flags)
        parcel.writeParcelable(asm, flags)
        parcel.writeParcelable(torpedo, flags)
        parcel.writeString(transports)
        parcel.writeValue(qty)
        parcel.writeValue(dive)
        parcel.writeValue(speed)
        parcel.writeValue(auto)
        parcel.writeValue(tonnage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sea> {
        override fun createFromParcel(parcel: Parcel): Sea {
            return Sea(parcel)
        }

        override fun newArray(size: Int): Array<Sea?> {
            return arrayOfNulls(size)
        }
    }
}
fun parseSeaResponseString(response: String): List<Sea> {
    val gson = GsonBuilder().create()
    val sea = gson.fromJson<List<Sea>>(response, Sea.SeaList::class.java)
    return sea
}