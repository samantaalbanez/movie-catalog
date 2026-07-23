package br.com.samantaalbanez.moviescatalog.ui.util

import java.util.Locale

internal fun Double?.toFormattedRating(): String {
    if (this == null || this == 0.0) return "N/A"
    return String.format(Locale.getDefault(), "%.1f", this)
}
