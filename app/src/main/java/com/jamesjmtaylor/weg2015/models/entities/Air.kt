package com.jamesjmtaylor.weg2015.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.jamesjmtaylor.weg2015.models.Equipment
import com.jamesjmtaylor.weg2015.models.EquipmentType

/**
 * Created by jtaylor on 2/26/18.
 */
@Entity(tableName = "air")
data class Air(override @PrimaryKey val id: Long = 0,
               override val name: String, val description: String? = null,
               val groupIconUrl: String? = null, val individualIconUrl: String? = null,
               override val photoUrl: String? = null,

               @Embedded(prefix = "gun") var gun: Gun? = null,
               @Embedded(prefix = "agm") var agm: Gun? = null,
               @Embedded(prefix = "aam") var aam: Gun? = null,
               @Embedded(prefix = "asm") var asm: Gun? = null,

               val speed: Int? = null, var auto: Int? = null, var ceiling: Int? = null,
               val weight: Int? = null) : Equipment, Parcelable {
    @Ignore
    override var type = EquipmentType.AIR

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Gun::class.java.classLoader),
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
        return id == e?.id && e.type == EquipmentType.AIR
    }

    class AirList : ArrayList<Air>()//Used for GSON deserialization

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(groupIconUrl)
        parcel.writeString(individualIconUrl)
        parcel.writeString(photoUrl)
        parcel.writeParcelable(gun, flags)
        parcel.writeParcelable(agm, flags)
        parcel.writeParcelable(aam, flags)
        parcel.writeParcelable(asm, flags)
        parcel.writeValue(speed)
        parcel.writeValue(auto)
        parcel.writeValue(ceiling)
        parcel.writeValue(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Air> {
        override fun createFromParcel(parcel: Parcel): Air {
            return Air(parcel)
        }

        override fun newArray(size: Int): Array<Air?> {
            return arrayOfNulls(size)
        }
    }
}

fun deserializeAirResponseString(response: String): List<Air> {
    val gson = GsonBuilder().create()
    val guns = gson.fromJson<List<Air>>(response, Air.AirList::class.java)
    return guns
}
