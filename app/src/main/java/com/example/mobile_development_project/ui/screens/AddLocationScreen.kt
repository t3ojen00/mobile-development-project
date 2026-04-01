package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_development_project.ui.components.reusable.CardFormComponent
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.viewModels.LocationViewModel
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.ui.components.reusable.Overlay
import com.example.mobile_development_project.ui.theme.errorMsg


@Composable
fun AddLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var showOverlay by remember { mutableStateOf(false) }


    // scrollable column
    Column(
        modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 10.dp)
        .verticalScroll(scrollState)
        // close keyboard when clicked outside
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { focusManager.clearFocus() }
        )

    ) {
        CardFormComponent(viewModel = viewModel)

        Spacer(modifier = Modifier.height(8.dp))

        // display error/success messages
        viewModel.uiMessage?.let { (message, type) ->
            if (type == MsgType.ERROR)
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                color =  errorMsg,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            // after delay, clear message
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(3500)
                viewModel.clearMessage()
            }
        }
        // save button
        PrimaryButton(
            label = "Save location",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            onClick = {
                val success = viewModel.saveLocation()
                if (success) {
                    showOverlay = true
                } },
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (showOverlay) {
        Overlay(
            message = "Your location has been saved and will be added on the map shortly!",
            onDismiss = {
                showOverlay = false
                viewModel.clearForm()
            }
        )
    }
}

