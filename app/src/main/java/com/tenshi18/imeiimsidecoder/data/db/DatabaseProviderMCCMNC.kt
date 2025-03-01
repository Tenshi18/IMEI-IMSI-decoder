package com.tenshi18.imeiimsidecoder.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProviderMccMnc {
    @Volatile
    private var INSTANCE: MccMncDatabase? = null

    fun getDatabase(context: Context): MccMncDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                MccMncDatabase::class.java,
                "mcc_mnc.sqlite3" // имя базы в internal storage
            )
                .createFromAsset("mcc_mnc.sqlite3") // имя файла в assets
                .build()
            INSTANCE = instance
            instance
        }
    }
}