package com.tenshi18.imeiimsidecoder.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MccMncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMccMnc(mccMnc: MccMnc)

    @Query("SELECT * FROM mcc_mnc_table WHERE mcc = :mcc AND mnc = :mnc")
    suspend fun getMccMnc(mcc: Int, mnc: Int): MccMnc?
}