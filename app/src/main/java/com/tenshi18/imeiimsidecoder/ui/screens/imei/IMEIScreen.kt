package com.tenshi18.imeiimsidecoder.ui.screens.imei

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.tenshi18.imeiimsidecoder.NavItem

@Composable
fun IMEIScreen(
    viewModel: IMEIViewModel = viewModel()
) {
    val imeiInput by viewModel.imeiInput.collectAsState()
    val imeiInfo by viewModel.imeiInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isError by viewModel.isError.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Декодер IMEI",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        InputTextField(
            value = imeiInput,
            onValueChange = { viewModel.setIMEI(it) },
            label = "IMEI",
            placeholder = "Введите IMEI (15 цифр)",
            isError = isError,
            errorMessage = if (isError) "Неверный формат IMEI" else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.decodeIMEI()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isError && imeiInput.isNotEmpty()
        ) {
            Text("Декодировать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ResultCard(
            title = "Информация об устройстве",
            content = imeiInfo?.toString(),
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Примечание: IMEI — международный идентификатор мобильного оборудования",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}