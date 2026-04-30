package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.example.mobile_development_project.data.models.DailyImageMetrics
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ImagesPerUserChart(
    metrics: Map<LocalDate, DailyImageMetrics>
) {
    val sorted = remember(metrics) {
        metrics.toSortedMap().toList()
    }

    val entries = remember(sorted) {
        sorted.mapIndexed { index, (_, data) ->

            val value = if (data.activeUsers > 0) {
                data.uploadedImages.toFloat() / data.activeUsers.toFloat()
            } else {
                0f
            }

            FloatEntry(index.toFloat(), value)
        }
    }

    val formatter = DateTimeFormatter.ofPattern("dd.M.")
    val labels = remember(sorted) {
        sorted.map { it.first.format(formatter) }
    }

    val model = remember(entries) {
        entryModelOf(entries)
    }

    Chart(
        chart = columnChart(),
        model = model,
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ ->
                "%.1f".format(value)
            }
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                labels.getOrNull(value.toInt()) ?: ""
            },
            guideline = null
        ),
        modifier = Modifier.fillMaxWidth()
    )
}