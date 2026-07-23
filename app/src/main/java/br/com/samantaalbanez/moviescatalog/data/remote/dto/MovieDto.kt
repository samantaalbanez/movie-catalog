package br.com.samantaalbanez.moviescatalog.data.remote.dto

internal data class MovieDto(
    val id: Int,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?
)
