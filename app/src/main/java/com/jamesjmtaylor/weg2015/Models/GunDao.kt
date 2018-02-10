package com.jamesjmtaylor.weg2015.Models

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

/**
 * Created by jtaylor on 2/10/18.
 */

@Dao
interface GunDao {

    @Query("select * from Gun")
    fun getAllGuns(): LiveData<List<Gun>>

    @Query("select * from Gun where id = :p0")
    fun findGunById(id: Long): LiveData<Gun>

    @Insert(onConflict = REPLACE)
    fun insertGun(Gun: Gun)

    @Update(onConflict = REPLACE)
    fun updateGun(Gun: Gun)

    @Delete
    fun deleteGun(Gun: Gun)
}