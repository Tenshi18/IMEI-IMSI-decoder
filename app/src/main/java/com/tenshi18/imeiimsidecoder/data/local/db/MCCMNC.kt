package com.tenshi18.imeiimsidecoder.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mcc_mnc_table")
data class MccMnc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mcc: Int,
    val mnc: Int,
    val plmn: String,
    val region: String,
    val country: String,
    val iso: String,
    val operator: String,
    val brand: String,
    val tadig: String,
    val bands: String
)