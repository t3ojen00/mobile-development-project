package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.ui.components.reusable.CardFormComponent
import com.example.mobile_development_project.ui.components.reusable.FormMode
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.theme.Attention
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.rejectButton
import com.example.mobile_development_project.viewModels.AddLocationViewModel

sealed class DialogState {
    data object None : DialogState()
    data object Delete : DialogState()
    data object Success : DialogState()
}

@Composable
fun EditLocationScreen(
    viewModel: AddLocationViewModel = viewModel(),
    navController: NavHostController,
    locationId: String?
)
{
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }

    LaunchedEffect(locationId) {
        if (!locationId.isNullOrBlank()) {
            viewModel.loadLocationForEdit(locationId)
        }
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            CardFormComponent(viewModel, navController, mode = FormMode.EDIT)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                PrimaryButton(
                    label = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.saveLocation {
                            dialogState = DialogState.Success
                        }
                    }
                )
                Spacer(modifier = Modifier.weight(0.1f))

                IconButton(
                    onClick = { dialogState = DialogState.Delete },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color = rejectButton)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
    when (dialogState) {

        DialogState.Delete -> {
            AlertDialog(
                onDismissRequest = { dialogState = DialogState.None },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialogState = DialogState.None
                            viewModel.deleteLocation {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { dialogState = DialogState.None }
                    ) {
                        Text("Cancel", color = OrangeAccent, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                text = { Text("Are you sure you want to delete this location?") }
            )
        }

        DialogState.Success -> {
            AlertDialog(
                onDismissRequest = {
                    dialogState = DialogState.None
                    viewModel.clearMessage()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialogState = DialogState.None
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            "OK",
                            color = OrangeAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = { Text("Location edit saved successfully!") },
                title = { Text("Success") }
            )
        }

        DialogState.None -> Unit
    }
}