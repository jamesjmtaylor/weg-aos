package com.jamesjmtaylor.weg2015.models.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.jamesjmtaylor.weg2015.models.entities.Sea


/**
 * Created by jtaylor on 2/10/18.
 */

@Dao
interface SeaDao {
    @Insert(onConflict = REPLACE)
    fun save(sea: Sea)

    @Query("SELECT * FROM sea WHERE id = :seaId")
    fun load(seaId: String): LiveData<Sea>

    @Query("select * from sea")
    fun getAllSeaLiveData(): LiveData<List<Sea>>

    @Query("select * from sea")
    fun getAllSea(): List<Sea>

    @Insert(onConflict = REPLACE)
    fun insertSea(seaList: List<Sea>)
}