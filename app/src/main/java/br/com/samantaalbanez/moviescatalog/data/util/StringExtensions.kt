package br.com.samantaalbanez.moviescatalog.data.util

internal fun String?.toImageUrl(quality: String = "w500"): String =
    this?.let { path ->
        "${Constants.BASE_IMAGE_URL}/$quality$path"
    }.orEmpty()
