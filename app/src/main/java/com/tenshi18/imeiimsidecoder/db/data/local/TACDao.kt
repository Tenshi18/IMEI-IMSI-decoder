package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TacDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTac(tac: Tac)

    @Query("SELECT * FROM tac_table WHERE tac = :tac")
    suspend fun getTac(tac: Int): Tac?

    @Query("SELECT * FROM tac_table")
    suspend fun getAllTacs(): List<Tac>
}