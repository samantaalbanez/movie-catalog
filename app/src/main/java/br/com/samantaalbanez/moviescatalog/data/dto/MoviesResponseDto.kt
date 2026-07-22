package br.com.samantaalbanez.moviescatalog.data.dto

internal data class MovieResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val totalPages: Int,
    val totalResults: Int
)
