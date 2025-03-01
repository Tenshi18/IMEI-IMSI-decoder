package com.tenshi18.imeiimsidecoder.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tac_table")
data class Tac(
    @PrimaryKey val tac: Int,
    val brand: String,
    val model: String,
    val aka: String?
)