package com.tenshi18.imeiimsidecoder.db.domain.repository

import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.remote.model.APIIMEIResponse

interface DeviceRepository {

    suspend fun getModelBrandNameFromAPI(imei: String): APIIMEIResponse

    suspend fun getTAC(tac: Int): TAC?
    suspend fun getMCCMNC(mcc: Int, mnc: Int): MCCMNC?
}

