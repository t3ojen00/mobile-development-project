package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.viewModels.AddLocationViewModel

@Composable
fun AddLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: AddLocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Add location screen")
        Card( modifier = Modifier
            .fillMaxSize()
        ) {
            Text("test")

        }
    }

}