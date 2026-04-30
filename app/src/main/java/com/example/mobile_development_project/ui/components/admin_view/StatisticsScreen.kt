package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_development_project.ui.components.reusable.SecondaryButton
import com.example.mobile_development_project.viewModels.AnalyticsViewModel

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel()) {

    val metrics by viewModel.dailyMetrics.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {

        item {
            Text(
                "Weekly analytics",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            //Text("$metrics")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SecondaryButton(
                    onClick = { viewModel.generateMockData() },
                    label = "Mock data",
                    modifier = Modifier
                        .padding(16.dp)
                )
                SecondaryButton(
                    onClick = { viewModel.clearMockData() },
                    label = "Clear mock data",
                    modifier = Modifier.padding(16.dp))
            }
        }

        item {
            Text(
                "Active users per day",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            UserActivityMetricsChart(metrics)
        }

        item{ Spacer(modifier = Modifier.height(20.dp)) }

        item {
            Text(
                "Average session duration per day",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center)
            MetricsChartAvgSession(metrics)
        }
    }
}
