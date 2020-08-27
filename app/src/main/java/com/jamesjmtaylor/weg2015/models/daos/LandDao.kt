package com.jamesjmtaylor.weg2015.models.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jamesjmtaylor.weg2015.models.entities.Land


/**
 * Created by jtaylor on 2/10/18.
 */

@Dao
interface LandDao {
    @Insert(onConflict = REPLACE)
    fun save(land: Land)

    @Query("SELECT * FROM land WHERE id = :landId")
    fun load(landId: String): LiveData<Land>

    @Query("select * from land")
    fun getAllLandLiveData(): LiveData<List<Land>>

    @Query("select * from land")
    fun getAllLand(): List<Land>

    //
//    @Query("select * from Land where id = :p0")
//    fun findLandById(id: Long): LiveData<Land>
//
//    @Insert(onConflict = REPLACE)
//    fun insertLand(Land: Land)
//
//    @Update(onConflict = REPLACE)
//    fun updateLand(Land: Land)
//
//    @Delete
//    fun deleteLand(Land: Land)
//
    @Insert(onConflict = REPLACE)
    fun insertLand(landList: List<Land>)
}