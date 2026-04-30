package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.data.models.User
import com.example.mobile_development_project.ui.components.admin_view.StatisticsScreen
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.viewModels.AdminViewModel
import com.example.mobile_development_project.ui.components.admin_view.PendingLocations
import com.example.mobile_development_project.ui.components.admin_view.RejectedLocations
import com.example.mobile_development_project.ui.components.admin_view.UsersList
import com.example.mobile_development_project.ui.components.reusable.ScrollToTopButton
import com.example.mobile_development_project.ui.theme.Attention
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.viewModels.AnalyticsViewModel
import kotlinx.coroutines.launch

sealed class Dialog {
    data object None : Dialog()
    data class DeleteUser(val user: User) : Dialog()
    data class DeleteLocation(val locationId: String) : Dialog()
}
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(),
    navController: NavHostController,
    role: String?
) {
    LaunchedEffect(Unit) {
        viewModel.fetchPendingLocations()
        viewModel.fetchAllUsers()
        viewModel.fetchRejectedLocations()
    }
    var selectedTab by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()
    val pendingState = rememberLazyListState()
    val usersState = rememberLazyListState()
    val rejectedState = rememberLazyListState()
    val statisticsState = rememberLazyListState()
    var dialogState by remember { mutableStateOf<Dialog>(Dialog.None) }
    val message = viewModel.uiMessage

    Box(modifier = Modifier.fillMaxSize()) {

        if (role == "admin") {
            Column(modifier = Modifier.fillMaxSize())
            {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFC9CBCF),
                    contentColor = Burgundy
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Pending") },
                        selectedContentColor = Burgundy,
                        unselectedContentColor = Color.DarkGray
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Users") },
                        selectedContentColor = Burgundy,
                        unselectedContentColor = Color.DarkGray
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Rejected") },
                        selectedContentColor = Burgundy,
                        unselectedContentColor = Color.DarkGray
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("Statistics") },
                    )
                }

                HorizontalDivider(color = Color.Gray)

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> PendingLocations(
                        viewModel,
                        navController,
                        role,
                        listState = pendingState
                    )

                    1 -> UsersList(
                        users = viewModel.allUsers,
                        viewModel = viewModel,
                        onDelete = { user ->
                            dialogState = Dialog.DeleteUser(user)
                        },
                        onPromote = { user, role ->
                            viewModel.updateUserRole(user.id, role)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        listState = usersState
                    )

                    2 -> RejectedLocations(
                        viewModel,
                        navController,
                        listState = rejectedState,
                        onDelete = { location ->
                            dialogState = Dialog.DeleteLocation(location.id)
                        }
                    )

                    3 -> StatisticsScreen(
                        viewModel = AnalyticsViewModel()
                    )
                }
            }
        } else if (role == "moderator") {
            PendingLocations(viewModel, navController, role, listState = pendingState)
        }

        val currentTabState = when (selectedTab) {
            0 -> pendingState
            1 -> usersState
            2 -> rejectedState
            3 -> statisticsState
            else -> pendingState
        }

        ScrollToTopButton(
            listState = currentTabState,
            onClick = {
                scope.launch {
                    currentTabState.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
    if (message?.second == MsgType.SUCCESS) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessage() },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearMessage() }
                ) {
                    Text("OK", color = OrangeAccent)
                }
            },
            title = { Text("Success") },
            text = { Text(message.first) }
        )
    }
    when (dialogState) {

        is Dialog.DeleteUser -> {
            val user = (dialogState as Dialog.DeleteUser).user

            AlertDialog(
                onDismissRequest = { dialogState = Dialog.None },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteUser(user.id)
                            dialogState = Dialog.None
                        }
                    ) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { dialogState = Dialog.None }
                    ) {
                        Text(
                            "Cancel",
                        color = OrangeAccent)
                    }
                },
                title = {
                    Text("Delete user",
                    color = MaterialTheme.colorScheme.error)
                        },
                text = {
                    Text(
                        buildAnnotatedString {
                            append("Are you sure you want to delete ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(user.username.takeIf { it.isNotBlank() } ?: user.email)
                            }
                            append("?")
                        }
                    )
                }
            )
        }
        is Dialog.DeleteLocation -> {
            val locationId = (dialogState as Dialog.DeleteLocation).locationId
            AlertDialog(
                onDismissRequest = { dialogState = Dialog.None },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteLocation(locationId)
                            dialogState = Dialog.None
                        })
                    { Text("Delete", color = Attention) }
                },
                dismissButton = {
                    TextButton(
                        onClick = { dialogState = Dialog.None }
                    ) { Text("Cancel", color = OrangeAccent) }
                },
                title = {
                    Text("Delete location", color = Attention) },
                text = {
                    Text("Are you sure you want to delete this location?") }
            )
        }
        Dialog.None -> Unit
    }
}


