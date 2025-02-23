package com.lust.app.presentation.addLocationModule.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lust.app.R

@Composable
fun SendButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(onClick = onClick) {
        Text(stringResource(R.string.btn_send))
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
    }
}