package com.tenshi18.imeiimsidecoder.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tac::class], version = 1, exportSchema = false)
abstract class TacDatabase : RoomDatabase() {
    abstract fun tacDao(): TacDao
}