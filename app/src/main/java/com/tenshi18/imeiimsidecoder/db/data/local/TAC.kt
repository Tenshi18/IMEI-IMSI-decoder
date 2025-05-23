package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tac", indices = [Index(value = ["tac"])])
data class TAC(
    @PrimaryKey val tac: Int?,
    val brand: String?,
    val model: String?,
    val aka: String?
)