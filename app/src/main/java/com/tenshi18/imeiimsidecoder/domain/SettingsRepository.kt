package com.tenshi18.imeiimsidecoder.domain

interface SettingsRepository {

    suspend fun isDynamicColorEnabled(): Boolean
    suspend fun setDynamicColorEnabled(value: Boolean)

}