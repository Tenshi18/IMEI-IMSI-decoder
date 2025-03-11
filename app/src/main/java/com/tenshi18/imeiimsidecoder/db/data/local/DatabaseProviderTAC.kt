package com.tenshi18.imeiimsidecoder.db.data.local

import android.content.Context
import androidx.room.Room
import kotlin.jvm.java

object DatabaseProviderTac {
    @Volatile
    private var INSTANCE: TACDatabase? = null

    fun getDatabase(context: Context): TACDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TACDatabase::class.java,
                "tac.sqlite3"
            )
                .createFromAsset("tac.sqlite3")
                .build()
            INSTANCE = instance
            instance
        }
    }
}