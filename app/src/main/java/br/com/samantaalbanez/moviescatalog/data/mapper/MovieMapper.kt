package br.com.samantaalbanez.moviescatalog.data.mapper

import br.com.samantaalbanez.moviescatalog.data.dto.MovieDto
import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal fun MovieDto.toDomain(): Movie {
    val posterBaseUrl = "https://image.tmdb.org/t/p/w500" // Largura 500 para posters
    val backdropBaseUrl = "https://image.tmdb.org/t/p/w780" // Largura 780 para banners

    return Movie(
        id = this.id,
        title = this.title ?: "Título não disponível",
        overview = if (this.overview.isNullOrEmpty()) "Sem sinopse disponível." else this.overview,
        posterUrl = if (this.posterPath != null) "$posterBaseUrl${this.posterPath}" else "",
        backdropUrl = if (this.backdropPath != null) "$backdropBaseUrl${this.backdropPath}" else "",
        releaseDate = this.releaseDate ?: "",
        voteAverage = this.voteAverage ?: 0.0
    )
}
