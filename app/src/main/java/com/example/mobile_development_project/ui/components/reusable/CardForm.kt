package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun CardFormComponent(
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            TextFieldComponent(
                value = name,
                onValueChange = { name = it},
                placeholder = "Location name",
                maxCharacters = 50,
                maxLines = 1,
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextFieldComponent(
                value = description,
                onValueChange = { description = it},
                placeholder = "Description",
                maxCharacters = 130,
                maxLines = 4,
                focusManager = focusManager,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                label = "Add tag",
                onClick = { /*TODO*/ },
            )
        }
    }
}