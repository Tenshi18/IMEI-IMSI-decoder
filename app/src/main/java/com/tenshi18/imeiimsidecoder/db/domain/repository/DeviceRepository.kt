package com.tenshi18.imeiimsidecoder.db.domain.repository

import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC

interface DeviceRepository {
    suspend fun getTAC(tac: Int): TAC?
    suspend fun getMCCMNC(mcc: Int, mnc: Int): MCCMNC?
}

