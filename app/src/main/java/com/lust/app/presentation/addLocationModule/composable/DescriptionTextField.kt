package com.lust.app.presentation.addLocationModule.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.lust.app.R

@Composable
fun DescriptionTextField(
    descriptionPlace: String,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.label_description_place)) },
            value = descriptionPlace,
            onValueChange = onDescriptionChange,
            maxLines = 6,
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
        Text(
            text = "${descriptionPlace.length}/400",
            modifier = Modifier.fillMaxWidth(),
            color = if (descriptionPlace.length > 400) Color.Red else Color.Gray
        )
    }
}