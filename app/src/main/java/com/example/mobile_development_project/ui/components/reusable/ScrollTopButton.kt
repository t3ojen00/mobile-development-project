package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.ui.theme.scrollButton

@Composable
fun ScrollToTopButton(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // listState object is used to get the current scroll position of the list
    val scrollInfo = listState.layoutInfo
    val isAtTop = listState.firstVisibleItemIndex == 0 &&
            listState.firstVisibleItemScrollOffset == 0

    // derived state is recalculated only when dependencies change
    val isAtBottom = remember(listState) {
        derivedStateOf {
            val lastVisible = scrollInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= scrollInfo.totalItemsCount - 1
        }
    }.value

    val isScrolling by remember {
        derivedStateOf {
            listState.isScrollInProgress
        }
    }
    // btn is visible only during active scrolling (but clickable), so e.g. at the bottom it doesn't block any elements
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(listState.isScrollInProgress, isAtBottom, isAtTop) {
        if (listState.isScrollInProgress) {
            showButton = true
        } else {
            kotlinx.coroutines.delay(1200)
            showButton = !isAtTop && !isAtBottom
        }
    }

    if (showButton) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = scrollButton,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(50),
            elevation = FloatingActionButtonDefaults.elevation(14.dp),
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll to top",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}