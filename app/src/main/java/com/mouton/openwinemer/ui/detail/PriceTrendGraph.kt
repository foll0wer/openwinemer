package com.mouton.openwinemer.ui.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.PriceEntryEntity
import java.time.LocalDate

/**
 * Simple line chart showing price evolution over time.
 */
@Composable
fun PriceTrendGraph(entries: List<PriceEntryEntity>) {
    if (entries.isEmpty()) return

    // Sort by date ascending
    val sorted = entries.sortedBy { LocalDate.parse(it.date) }
    val prices = sorted.map { it.price }

    val maxPrice = prices.maxOrNull() ?: 1.0
    val minPrice = prices.minOrNull() ?: 0.0
    val range = (maxPrice - minPrice).coerceAtLeast(0.01)

    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.secondary

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
    ) {
        val stepX = size.width / (prices.size - 1).coerceAtLeast(1)

        val points = prices.mapIndexed { index, price ->
            val x = index * stepX
            val y = size.height - ((price - minPrice) / range * size.height)
            Offset(x, y.toFloat())
        }

        // Draw connecting lines
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        // Draw points
        points.forEach {
            drawCircle(
                color = pointColor,
                radius = 6f,
                center = it
            )
        }
    }
}
