package com.jamesjmtaylor.weg2015.models.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
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