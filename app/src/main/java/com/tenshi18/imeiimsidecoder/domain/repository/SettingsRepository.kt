package com.tenshi18.imeiimsidecoder.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val useDynamicColoursFlow: Flow<Boolean>
    suspend fun setDynamicColoursEnabled(enabled: Boolean)
}