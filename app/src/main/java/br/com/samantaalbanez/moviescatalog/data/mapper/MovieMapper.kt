package br.com.samantaalbanez.moviescatalog.data.mapper

import br.com.samantaalbanez.moviescatalog.data.dto.MovieDto
import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal fun MovieDto.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title ?: "Título não disponível",
        overview = if (this.overview.isNullOrEmpty()) "Sem sinopse disponível." else this.overview,
        posterUrl = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = this.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" } ?: "",
        releaseDate = this.releaseDate ?: "",
        voteAverage = this.voteAverage ?: 0.0
    )
}
