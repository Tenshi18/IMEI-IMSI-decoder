package com.tenshi18.imeiimsidecoder.db.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "mcc_mnc", primaryKeys = ["mcc", "mnc"], indices = [Index(value = ["mcc", "mnc"])])
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