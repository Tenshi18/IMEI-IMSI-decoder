package com.tenshi18.imeiimsidecoder.history.utils

import androidx.compose.ui.text.AnnotatedString
import com.tenshi18.imeiimsidecoder.db.data.local.MCCMNC
import com.tenshi18.imeiimsidecoder.db.data.local.TAC

fun formatIMEIResult(imeiResult: TAC?): AnnotatedString {
    return AnnotatedString("""        
            Бренд: ${imeiResult?.brand}
            Модель: ${imeiResult?.model}
            AKA: ${imeiResult?.aka}
        """.trimIndent()
    )
}

fun formatIMSIResult(imsiResult: MCCMNC?): AnnotatedString {
    return AnnotatedString("""
        MCC: ${imsiResult?.mcc}
        MNC: ${imsiResult?.mnc}
        PLMN: ${imsiResult?.plmn}
        Регион: ${imsiResult?.region}
        Страна: ${imsiResult?.country}
        ISO-код страны: ${imsiResult?.iso}
        Оператор: ${imsiResult?.operator}
        Бренд: ${imsiResult?.brand}
        Частоты: ${imsiResult?.bands}
        """.trimIndent()
    )
}