package br.com.samantaalbanez.moviescatalog.data.remote.dto

internal data class MoviesResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val totalPages: Int,
    val totalResults: Int
)
