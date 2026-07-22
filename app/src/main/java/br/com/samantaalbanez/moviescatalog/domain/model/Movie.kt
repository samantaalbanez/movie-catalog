package br.com.samantaalbanez.moviescatalog.domain.model

internal data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val isFavorite: Boolean = false
)