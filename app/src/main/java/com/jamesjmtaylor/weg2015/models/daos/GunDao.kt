package com.jamesjmtaylor.weg2015.models.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.jamesjmtaylor.weg2015.models.entities.Gun


/**
 * Created by jtaylor on 2/10/18.
 */

@Dao
interface GunDao {
    @Insert(onConflict = REPLACE)
    fun save(gun: Gun)

    @Query("SELECT * FROM gun WHERE id = :gunId")
    fun load(gunId: String): LiveData<Gun>

    @Query("select * from gun")
    fun getAllGunsLiveData(): LiveData<List<Gun>>

    @Query("select * from gun")
    fun getAllGuns(): List<Gun>

    @Insert(onConflict = REPLACE)
    fun insertGuns(gunList: List<Gun>)
}