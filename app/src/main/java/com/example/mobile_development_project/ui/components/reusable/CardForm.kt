package com.example.mobile_development_project.ui.components.reusable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.ui.theme.cardColor
import com.example.mobile_development_project.viewModels.AddLocationViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.ErrorCause
import com.example.mobile_development_project.navigation.NavRoutes
import android.net.Uri
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.mobile_development_project.helpers.createImageUri
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

enum class FormMode {
    ADD,
    EDIT
}

// "create" a picture by taking it with camera
fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        File(context.cacheDir, "images").apply { mkdirs() }
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@SuppressLint("MissingPermission")
@Composable
fun CardFormComponent(
    viewModel: AddLocationViewModel,
    navController: NavHostController,
    mode: FormMode = FormMode.ADD,
) {

    val focusManager = LocalFocusManager.current
    var showTagInput by remember { mutableStateOf(false) }
    var tagInput by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val images = remember { mutableStateListOf<String?>(null, null, null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.images) {
        for (i in 0..2) {
            images[i] = viewModel.images.getOrNull(i)
        }
    }
    // gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val emptyIndex = images.indexOfFirst { it == null }
            if (emptyIndex != -1) {
                images[emptyIndex] = uri.toString()
                viewModel.addImage(uri.toString())
            }
        }
    }
    // cmaera
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            val emptyIndex = images.indexOfFirst { it == null }
            if (emptyIndex != -1) {
                images[emptyIndex] = cameraImageUri.toString()
                viewModel.addImage(cameraImageUri.toString())
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraImageUri = uri
            takePictureLauncher.launch(uri)
        } else {
            viewModel.setError(ErrorCause.LOCATION_PERMISSION_DENIED)
        }
    }

    // listen and receive selected coordinates (lat, lng) from MapSelectionScreen and store in ViewModel
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.getLiveData<Pair<Double, Double>>("selected_location")?.observe(LocalLifecycleOwner.current) {
        selectedCoordinates ->
            viewModel.setSelectedLocation(selectedCoordinates.first, selectedCoordinates.second)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission())
    { granted ->
        if (!granted) {
            viewModel.setError(ErrorCause.LOCATION_PERMISSION_DENIED)
            return@rememberLauncherForActivityResult
        }
        isLoading = true

        val cancellationToken = CancellationTokenSource().token
        val client = LocationServices.getFusedLocationProviderClient(context)

        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
            .addOnSuccessListener { location ->
                isLoading = false
                if (location != null) {
                    Log.d("GPS", "SOURCE LAT=${location.latitude}, LNG=${location.longitude}")
                    viewModel.setGpsCoordinates(location.latitude, location.longitude)
                } else {
                    // call works technically but location is null
                    viewModel.setError(ErrorCause.LOCATION_UNAVAILABLE)
                }
            }
            // whole call fails
            .addOnFailureListener {
                viewModel.setError(ErrorCause.LOCATION_FETCH_FAILED, exception = it)
                isLoading = false
            }
      }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp, horizontal = 18.dp)
        ) {

            if (mode == FormMode.EDIT) {
                ImageCarousel(
                    items = images,
                    editMode = true,
                    onRemoveImage = { index ->
                        val uriToRemove = images[index]
                        if (uriToRemove != null) {
                            viewModel.removeImage(uriToRemove)
                        }
                        images[index] = null
                    },
                    onAddImage = null
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            TextFieldComponent(
                value = viewModel.locationName,
                onValueChange = { viewModel.onLocationNameChange(it)},
                placeholder = "Location name",
                maxCharacters = 50,
                maxLines = 1,
                focusManager = focusManager,
                trailingIcon =
                    if (mode == FormMode.EDIT) {
                    { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                    } else null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextFieldComponent(
                value = viewModel.description,
                onValueChange = { viewModel.onDescriptionChange(it)},
                placeholder = "Description",
                maxCharacters = 130,
                maxLines = 4,
                focusManager = focusManager,
                trailingIcon =
                    if (mode == FormMode.EDIT) {
                        { Icon(
                            Icons.Default.Edit, contentDescription = "Edit") }
                    } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                )
            Spacer(modifier = Modifier.height(12.dp))

            if (!showTagInput) {
                PrimaryButton(
                    label = "Add tag",
                    onClick = { showTagInput = true },
                    modifier = Modifier.width(100.dp)
                    )
            }
            if (showTagInput) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextFieldComponent(
                        value = tagInput,
                        onValueChange = { tagInput = it },
                        placeholder = "Enter tag",
                        maxCharacters = 20,
                        maxLines = 1,
                        focusManager = focusManager,
                        modifier = Modifier
                            .weight(1f) // reserves 1/2 of space
                            .padding(end = 8.dp),
                        )

                    Spacer(modifier = Modifier.width(12.dp))

                    PrimaryButton(
                        label = "Save tag",
                        enabled = tagInput.isNotBlank(), // input field can't be empty
                        onClick = {
                            viewModel.addTag(tagInput)
                            tagInput = ""
                            showTagInput = false },
                        modifier = Modifier.weight(1f) // reserves 1/2 of space
                    )
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            // tags displayed in row that wraps them
            val tagsPerRow = 2
            // splits collection into a list of lists that don't exceed given size
            val  rows = viewModel.tags.chunked(tagsPerRow)

            Column {
                rows.forEach { rowTags ->
                    Row( horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowTags.forEach { tag ->
                            TagComponent(tag, onDeleteTag = { viewModel.removeTag(tag) })
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp)) // vertical space between tags
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            Row( modifier = Modifier.fillMaxWidth()) {
                if (mode == FormMode.ADD) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.TopStart)
                    ) {
                        PrimaryButton(
                            label = "Select location",
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            Modifier.padding(4.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Select from map") },
                                onClick = {
                                    expanded = false
                                    navController.navigate(NavRoutes.SelectFromMap)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Allow access to GPS") },
                                onClick = {
                                    expanded = false
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        PrimaryButton(
                            label = "Add images",
                            onClick = { imageMenuExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = imageMenuExpanded,
                            onDismissRequest = { imageMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Choose from gallery") },
                                onClick = {
                                    imageMenuExpanded = false
                                    if (images.count { it != null } < 3) {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Take a picture") },
                                onClick = {
                                    imageMenuExpanded = false
                                    if (images.count { it != null } < 3) {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            )
                        }
                    }
                }
            }
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


            if (mode == FormMode.ADD) {
                Spacer(modifier = Modifier.height(20.dp))
                ImageCarousel(
                    items = images,
                    editMode = true,
                    onRemoveImage = { index ->
                        val uriToRemove = images[index]
                        if (uriToRemove != null) {
                            viewModel.removeImage(uriToRemove)
                        }
                        images[index] = null
                    },
                    onAddImage = null
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}