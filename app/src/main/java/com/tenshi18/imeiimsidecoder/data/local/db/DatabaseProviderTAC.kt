package com.tenshi18.imeiimsidecoder.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProviderTac {
    @Volatile
    private var INSTANCE: TacDatabase? = null

    fun getDatabase(context: Context): TacDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TacDatabase::class.java,
                "tac.sqlite3"
            )
                .createFromAsset("tac.sqlite3")
                .build()
            INSTANCE = instance
            instance
        }
    }
}