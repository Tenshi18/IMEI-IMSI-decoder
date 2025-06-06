package com.tenshi18.imeiimsidecoder.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceEntry(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: String? = null,
    content: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .alpha(if (isEnabled) 1f else 0.5f)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                icon()
            }
            Spacer(Modifier.width(12.dp))
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                title()
            }
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            content?.invoke()
        }

        if (trailingContent != null) {
            Spacer(Modifier.width(12.dp))
            trailingContent()
        }
    }
}

@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
) {
    PreferenceEntry(
        modifier = modifier,
        title = title,
        description = description,
        icon = icon,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = isEnabled
            )
        },
        onClick = { onCheckedChange(!checked) },
        isEnabled = isEnabled
    )
}

@Composable
fun PreferenceGroupTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(16.dp)
    )
}