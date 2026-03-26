package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.ui.components.reusable.CardFormComponent
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.viewModels.LocationViewModel

@Composable
fun AddLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        CardFormComponent(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        // save button
        PrimaryButton(
            label = "Save location",
            onClick = { viewModel.saveLocation() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}