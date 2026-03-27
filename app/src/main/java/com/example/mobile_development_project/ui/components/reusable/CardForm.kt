package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.mobile_development_project.viewModels.LocationViewModel
import androidx.compose.foundation.layout.Row

@Composable
fun CardFormComponent(
    viewModel: LocationViewModel
) {

    val focusManager = LocalFocusManager.current
    var showTagInput by remember { mutableStateOf(false) }
    var tagInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
            TextFieldComponent(
                value = viewModel.locationName,
                onValueChange = { viewModel.onLocationNameChange(it)},
                placeholder = "Location name",
                maxCharacters = 50,
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextFieldComponent(
                value = viewModel.description,
                onValueChange = { viewModel.onDescriptionChange(it)},
                placeholder = "Description",
                maxCharacters = 130,
                maxLines = 4,
                focusManager = focusManager,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            Spacer(modifier = Modifier.height(16.dp))

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
                                showTagInput = false
                            },
                        modifier = Modifier.weight(1f) // reserves 1/2 of space
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

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
            Spacer(modifier = Modifier.height(16.dp))

            Row( modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    label = "Select location",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                    )

                Spacer(modifier = Modifier.width(16.dp))

                PrimaryButton(
                    label = "Add images",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ImagePlaceholder(
                    modifier = Modifier.weight(1f)
                )

                ImagePlaceholder(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}