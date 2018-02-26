package com.jamesjmtaylor.weg2015.models.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.jamesjmtaylor.weg2015.models.entities.Air


/**
 * Created by jtaylor on 2/10/18.
 */

@Dao
interface AirDao {
    @Insert(onConflict = REPLACE)
    fun save(air: Air)

    @Query("SELECT * FROM Air WHERE id = :airId")
    fun load(airId: String): LiveData<Air>

    @Query("select * from Air")
    fun getAllAirLiveData(): LiveData<List<Air>>

    @Query("select * from Air")
    fun getAllAir(): List<Air>
//
//    @Query("select * from Gun where id = :p0")
//    fun findGunById(id: Long): LiveData<Gun>
//
//    @Insert(onConflict = REPLACE)
//    fun insertGun(Gun: Gun)
//
//    @Update(onConflict = REPLACE)
//    fun updateGun(Gun: Gun)
//
//    @Delete
//    fun deleteGun(Gun: Gun)
//
    @Insert(onConflict = REPLACE)
    fun insertAir(airList: List<Air>)
}