package com.lust.app.presentation.addLocationModule

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lust.app.R
import com.lust.app.data.entities.PlaceType
import com.lust.app.presentation.addLocationModule.composable.DescriptionTextField
import com.lust.app.presentation.addLocationModule.composable.NameTextField
import com.lust.app.presentation.addLocationModule.composable.PlaceFolder
import com.lust.app.presentation.addLocationModule.composable.PlaceModalBottomSheet
import com.lust.app.presentation.addLocationModule.composable.SendButton
import com.lust.app.presentation.navigation.LocalNavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    modifier: Modifier = Modifier,
    lat: Double,
    lng: Double,
    vm: AddLocationViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current
    val state by remember { vm.state }.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var placeType by remember { mutableStateOf(PlaceType.BAR) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    var namePlace by remember { mutableStateOf("") }
    var descriptionPlace by remember { mutableStateOf("") }

    if (AddLocationState.NamePlaceEmpty == state) coroutineScope.launch {
        snackbarHostState.showSnackbar("Agrega un nombre")
    }

    if (AddLocationState.DescriptionPlaceEmpty == state) coroutineScope.launch {
        snackbarHostState.showSnackbar("Agrega una descripción")
    }

    if (AddLocationState.SavePlace == state) navController.popBackStack()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(stringResource(R.string.title_add_place)) },
                actions = {
                    IconButton(onClick = {
                        vm.launchIntent(
                            AddLocationIntent.RegisterPlace(
                                categoryId = placeType.id,
                                namePlace = namePlace,
                                descriptionPlace = descriptionPlace,
                                latitude = lat,
                                longitude = lng
                            )
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                PlaceFolder(itemSelect = placeType) { openBottomSheet = !openBottomSheet }
                NameTextField(namePlace = namePlace, onNameChange = { namePlace = it })
                DescriptionTextField(descriptionPlace = descriptionPlace, onDescriptionChange = {
                    if (it.length <= 400 && it.lines().size <= 6) {
                        descriptionPlace = it
                    }
                })
                SendButton(onClick = {
                    vm.launchIntent(
                        AddLocationIntent.RegisterPlace(
                            categoryId = placeType.id,
                            namePlace = namePlace,
                            descriptionPlace = descriptionPlace,
                            latitude = lat,
                            longitude = lng
                        )
                    )
                })
            }
        }
    )

    if (openBottomSheet) PlaceModalBottomSheet(
        bottomSheetState = bottomSheetState, onDismiss = {
            openBottomSheet = !openBottomSheet
        }, onItemSelect = { placeType = it })
}