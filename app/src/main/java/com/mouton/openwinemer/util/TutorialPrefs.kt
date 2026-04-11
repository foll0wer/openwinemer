package com.mouton.openwinemer.util

import android.content.Context

/**
 * Simple SharedPreferences helper to store tutorial completion flags.
 * Each screen uses its own key.
 */
object TutorialPrefs {

    private const val PREFS_NAME = "tutorial_prefs"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun hasSeen(context: Context, key: String): Boolean {
        return prefs(context).getBoolean(key, false)
    }

    fun setSeen(context: Context, key: String) {
        prefs(context).edit().putBoolean(key, true).apply()
    }
}
