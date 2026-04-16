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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_development_project.ui.components.reusable.CardFormComponent
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.viewModels.AddLocationViewModel
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.ui.components.reusable.Overlay
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.errorMsg
import com.example.mobile_development_project.ui.theme.successMsg


@Composable
fun AddLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: AddLocationViewModel = viewModel(),
    navController: NavHostController
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var showSuccess by remember { mutableStateOf(false) }

        // scrollable column
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(scrollState)
                // close keyboard when clicked outside
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { focusManager.clearFocus() }
                )

        ) {
            CardFormComponent(viewModel = viewModel, navController = navController)

            Spacer(modifier = Modifier.height(12.dp))

            viewModel.uiMessage?.let { (message, type) ->
                Text(
                    text = message,
                    //fontWeight = FontWeight.Bold,
                    color = if (type == MsgType.ERROR) errorMsg else successMsg,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                )
                // after delay, clear message
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(5000)
                    viewModel.clearMessage()
                }
            }
            // save button
            PrimaryButton(
                label = "Save location",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                onClick = {
                        viewModel.saveLocation {
                            showSuccess = true
                        }
                    },
                modifier = Modifier.fillMaxWidth()
            )
        }

            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = {
                        showSuccess = false
                        viewModel.clearForm()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSuccess = false
                                viewModel.clearForm()
                            }
                        ) {
                            Text(
                                "OK",
                                fontWeight = FontWeight.Bold,
                                color = successMsg
                            )
                        }
                    },
                    title = { Text("Success") },
                    text = {
                        Column {
                            Text("Your location has been saved and will be added on the map shortly!")
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Note that moderator can reject or request edits",
                                color = OrangeAccent,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )
            }
        }
