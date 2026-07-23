package br.com.samantaalbanez.moviescatalog.data.dto

internal data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double?,
    val releaseDate: String?,
    val runtime: Int?,
    val genres: List<GenreDto>?
)

