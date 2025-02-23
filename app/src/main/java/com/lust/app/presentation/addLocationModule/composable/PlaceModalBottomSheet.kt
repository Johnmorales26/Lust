package com.lust.app.presentation.addLocationModule.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lust.app.data.entities.PlaceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceModalBottomSheet(
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    onItemSelect: (PlaceType) -> Unit
) {
    val placeTypes = PlaceType.entries.toTypedArray()
    val filteredPlaces = placeTypes.filter { it.id != 0 }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredPlaces.size) { index ->
                val place = filteredPlaces[index]
                ListItem(
                    modifier = Modifier.clickable { onItemSelect(place) },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    headlineContent = { Text(stringResource(place.title)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(place.image),
                            contentDescription = null,
                            tint = place.color
                        )
                    }
                )
            }
        }
    }
}