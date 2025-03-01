package com.tenshi18.imeiimsidecoder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MccMnc::class], version = 1, exportSchema = false)
abstract class MccMncDatabase : RoomDatabase() {
    abstract fun mccMncDao(): MccMncDao
}