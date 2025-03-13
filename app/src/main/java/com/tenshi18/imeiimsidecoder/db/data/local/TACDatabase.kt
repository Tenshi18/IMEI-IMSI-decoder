package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TAC::class], version = 2, exportSchema = false)
abstract class TACDatabase : RoomDatabase() {
    abstract fun tacDao(): TACDao
}