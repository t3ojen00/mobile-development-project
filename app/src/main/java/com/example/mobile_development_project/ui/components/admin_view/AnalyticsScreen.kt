package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_development_project.statistics.ui.MetricsChartAvgSession
import com.example.mobile_development_project.statistics.ui.UserActivityMetricsChart
import com.example.mobile_development_project.viewModels.AnalyticsViewModel

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel()) {

    val metrics by viewModel.dailyMetrics.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        item {
            Text("Daily analytics")
        }

        /*item {
            UserActivityMetricsChart(metrics)
        }

        item {
            MetricsChartAvgSession(metrics)
        }*/
    }
}
