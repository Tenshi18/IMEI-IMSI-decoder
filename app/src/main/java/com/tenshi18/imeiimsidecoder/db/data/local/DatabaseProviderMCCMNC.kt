package com.tenshi18.imeiimsidecoder.db.data.local

import android.content.Context
import androidx.room.Room
import kotlin.jvm.java

object DatabaseProviderMCCMNC {
    @Volatile
    private var INSTANCE: MCCMNCDatabase? = null

    fun getDatabase(context: Context): MCCMNCDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MCCMNCDatabase::class.java,
                "mcc_mnc.sqlite3" // имя базы в internal storage
            )
                .createFromAsset("mcc_mnc.sqlite3") // имя файла в assets
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}