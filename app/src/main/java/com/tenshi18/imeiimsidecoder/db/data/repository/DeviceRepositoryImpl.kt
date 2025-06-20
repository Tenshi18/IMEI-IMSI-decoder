package com.tenshi18.imeiimsidecoder.db.data.repository

import android.content.Context
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNCDao
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.data.local.TACDao
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository
import com.tenshi18.imeiimsidecoder.remote.client.IMEIAPIClient
import com.tenshi18.imeiimsidecoder.remote.model.APIIMEIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val tacDao: TACDao,
    private val mccMncDao: MCCMNCDao,
    private val context: Context
) : DeviceRepository {

    override suspend fun getModelBrandNameFromAPI(imei: String): APIIMEIResponse =
        withContext(Dispatchers.IO) {
            val service = IMEIAPIClient.createService(context)
            service.getModelBrandName(imei)
        }

    override suspend fun getTAC(tac: Int): TAC? = tacDao.getTAC(tac)

    override suspend fun getMCCMNC(mcc: Int, mnc: Int): MCCMNC? = mccMncDao.getMCCMNC(mcc, mnc)
}
