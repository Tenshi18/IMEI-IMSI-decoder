package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MCCMNC::class], version = 3, exportSchema = false)
abstract class MCCMNCDatabase : RoomDatabase() {
    abstract fun mccMncDao(): MCCMNCDao
}