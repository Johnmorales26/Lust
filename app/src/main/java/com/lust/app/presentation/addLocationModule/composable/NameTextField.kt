package com.lust.app.presentation.addLocationModule.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lust.app.R

@Composable
fun NameTextField(
    namePlace: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.label_name_place)) },
        value = namePlace,
        onValueChange = onNameChange
    )
}