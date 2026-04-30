package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.mobile_development_project.data.models.DailyMetrics
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.toSortedMap
import com.example.mobile_development_project.helpers.formatDuration
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider

@Composable
fun UserActivityMetricsChart(
    metrics: Map<LocalDate, DailyMetrics>
) {
    // sorts the map by date so the chart displays values in chronological order
    val sorted = remember(metrics) { metrics.toSortedMap() }
    // converts each day’s metrics into chart entries with an X (index) and Y (ratio)
    val entries = sorted.entries.mapIndexed { index, (date, data) ->
        // active user ratio (active users / total users)
        val ratio = if (data.totalUsers > 0) {
            data.activeUsers.toFloat() / data.totalUsers.toFloat()
        } else 0f
        // chart point with values between 0% and 100%
        FloatEntry(index.toFloat(), ratio.coerceIn(0f, 1f))
    }

    val model = entryModelOf(entries) // data wrapped into chart model

    val formatter = DateTimeFormatter.ofPattern("dd.M.")
    val labels = sorted.keys.map { it.format(formatter) }

    // y-axis values as percentages
    val startAxis = rememberStartAxis(
        valueFormatter = { value, _ -> "${(value * 100).toInt()}%" },
        itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6),
    )

    val chart = columnChart(
        axisValuesOverrider = AxisValuesOverrider.fixed(
            minY = 0f,
            maxY = 1f,
        )
    )
    // chart rendering with column bars and  prepared dataset
    Chart(
        chart = chart,
        model = model,
        startAxis = startAxis,
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                labels.getOrNull(value.toInt()) ?: "" },
            itemPlacer = AxisItemPlacer.Horizontal.default(
                spacing = 1 ),
                guideline = null,
        )
    )
}

@Composable
fun MetricsChartAvgSession(
    metrics: Map<LocalDate, DailyMetrics>
) {

    val sorted = remember(metrics) {
        metrics.toSortedMap().toList()
    }

    val entries = remember(sorted) {
        sorted.mapIndexed { index, (_, data) ->
            FloatEntry(index.toFloat(), data.avgSessionDuration.toFloat())
        }
    }

    val formatter = DateTimeFormatter.ofPattern("dd.M.")
    val labels = remember(sorted) { sorted.map { it.first.format(formatter) }}
    val sessionModel = remember(entries ) {
        entryModelOf(entries)
    }

    Chart(
        chart = columnChart(),
        model = sessionModel,
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ ->
                formatDuration(value.toDouble()) },
            ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                labels.getOrNull(value.toInt()) ?: "" },
            guideline = null
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
