package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TACDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTAC(tac: TAC)

    @Query("SELECT * FROM tac_table WHERE tac = :tac LIMIT 1")
    suspend fun getTAC(tac: Int): TAC?

    @Query("SELECT * FROM tac_table")
    suspend fun getAllTACs(): List<TAC>
}