package com.tenshi18.imeiimsidecoder.db.data.repository

import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNCDao
import com.tenshi18.imeiimsidecoder.db.data.local.TAC
import com.tenshi18.imeiimsidecoder.db.data.local.TACDao
import com.tenshi18.imeiimsidecoder.db.domain.repository.DeviceRepository

class DeviceRepositoryImpl(
    private val tacDao: TACDao,
    private val mccMncDao: MCCMNCDao

) : DeviceRepository {
    override suspend fun getTAC(tac: Int) : TAC? = tacDao.getTAC(tac)
    override suspend fun getMCCMNC(mcc: Int, mnc: Int) : MCCMNC? = mccMncDao.getMCCMNC(mcc, mnc)
}