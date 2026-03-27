package com.mouton.openwinemer.ui.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mouton.openwinemer.data.model.PriceEntryEntity
import java.time.LocalDate
import kotlin.math.roundToInt
import com.mouton.openwinemer.R

/**
 * Price trend graph with:
 * - X and Y axes
 * - Tap detection
 * - Source-colored legend
 */
@Composable
fun PriceTrendGraph(entries: List<PriceEntryEntity>) {
    if (entries.isEmpty()) return

    // Colors resolved OUTSIDE Canvas
    val lineColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Assign a unique color per source
    val sourceColors = remember(entries) {
        val uniqueSources = entries.map { it.source }.distinct()
        uniqueSources.associateWith {
            Color(
                red = (50..200).random(),
                green = (50..200).random(),
                blue = (50..200).random()
            )
        }
    }

    var selectedEntry by remember { mutableStateOf<PriceEntryEntity?>(null) }

    // Sort entries by date
    val sorted = entries.sortedBy { LocalDate.parse(it.date) }
    val prices = sorted.map { it.price }

    val maxPrice = prices.maxOrNull() ?: 1.0
    val minPrice = prices.minOrNull() ?: 0.0
    val midPrice = (maxPrice + minPrice) / 2

    val firstDate = sorted.first().date
    val lastDate = sorted.last().date

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        // --- GRAPH CANVAS ---
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .pointerInput(entries) {
                    detectTapGestures { tapOffset ->
                        val stepX = size.width / (prices.size - 1).coerceAtLeast(1)
                        val index = (tapOffset.x / stepX).roundToInt()
                        if (index in sorted.indices) {
                            selectedEntry = sorted[index]
                        }
                    }
                }
        ) {
            val stepX = size.width / (prices.size - 1).coerceAtLeast(1)
            val range = (maxPrice - minPrice).coerceAtLeast(0.01)

            val points = prices.mapIndexed { index, price ->
                val x = index * stepX
                val y = size.height - ((price - minPrice) / range * size.height)
                Offset(x, y.toFloat())
            }

            // Y AXIS
            drawLine(
                color = axisColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 3f
            )

            // X AXIS
            drawLine(
                color = axisColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 3f
            )

            // Connecting lines
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = lineColor,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = 4f
                )
            }

            // Points (colored by source)
            points.forEachIndexed { index, offset ->
                val entry = sorted[index]
                val color = sourceColors[entry.source] ?: Color.Gray

                drawCircle(
                    color = color,
                    radius = 6f,
                    center = offset
                )
            }
        }

        // --- X AXIS LABELS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(firstDate, style = MaterialTheme.typography.labelSmall)
            Text(lastDate, style = MaterialTheme.typography.labelSmall)
        }

        Spacer(Modifier.height(4.dp))

        // --- Y AXIS LABELS ---
        Column {
            Text("↑ $maxPrice €", style = MaterialTheme.typography.labelSmall)
            Text("~ $midPrice €", style = MaterialTheme.typography.labelSmall)
            Text("↓ $minPrice €", style = MaterialTheme.typography.labelSmall)
        }

        // --- LEGEND ON TAP ---
        selectedEntry?.let { entry ->
            Card(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(sourceColors[entry.source] ?: Color.Gray)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("${stringResource(R.string.price_label)}: ${entry.price} €")
                        Text("${stringResource(R.string.date_label)}: ${entry.date}")
                        Text("${stringResource(R.string.source_label)}: ${entry.source}")
                    }
                }
            }
        }
    }
}
