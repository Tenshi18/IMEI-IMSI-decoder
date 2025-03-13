package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Entity

@Entity(tableName = "mcc_mnc", primaryKeys = ["mcc", "mnc"])
data class MCCMNC(
    val mcc: Int,
    val mnc: Int,
    val plmn: String?,
    val region: String?,
    val country: String?,
    val iso: String?,
    val operator: String?,
    val brand: String?,
    val tadig: String?,
    val bands: String?
)