package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MCCMNCDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMccMnc(MCCMNC: MCCMNC)

    @Query("SELECT * FROM mcc_mnc_table WHERE mcc = :mcc AND mnc = :mnc LIMIT 1")
    suspend fun getMCCMNC(mcc: Int, mnc: Int): MCCMNC?
}