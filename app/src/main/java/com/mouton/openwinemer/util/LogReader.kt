package com.mouton.openwinemer.util

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Reads the last N lines of logcat for this app.
 */
object LogReader {

    fun readLogs(maxLines: Int = 300): String {
        return try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            val lines = reader.readLines()
                .filter { it.contains("com.mouton.openwinemer") }
                .takeLast(maxLines)

            lines.joinToString("\n")
        } catch (e: Exception) {
            "Unable to read logs: ${e.message}"
        }
    }
}
