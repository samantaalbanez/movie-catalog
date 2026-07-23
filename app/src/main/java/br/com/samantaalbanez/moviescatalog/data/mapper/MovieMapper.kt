package br.com.samantaalbanez.moviescatalog.data.mapper

import br.com.samantaalbanez.moviescatalog.data.dto.MovieDetailsDto
import br.com.samantaalbanez.moviescatalog.data.dto.MovieDto
import br.com.samantaalbanez.moviescatalog.data.util.Constants.IMAGE_QUALITY_W500
import br.com.samantaalbanez.moviescatalog.data.util.Constants.IMAGE_QUALITY_W780
import br.com.samantaalbanez.moviescatalog.data.util.toImageUrl
import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal fun MovieDto.toDomain(): Movie =
    Movie(
        id = this.id,
        title = this.title ?: "Título não disponível",
        overview = if (this.overview.isNullOrEmpty()) "Sem sinopse disponível." else this.overview,
        posterUrl = posterPath.toImageUrl(IMAGE_QUALITY_W500),
        backdropUrl = backdropPath.toImageUrl(IMAGE_QUALITY_W780),
        releaseDate = this.releaseDate.orEmpty(),
        voteAverage = this.voteAverage ?: 0.0
    )

internal fun MovieDetailsDto.detailsToDomain(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview.orEmpty(),
        posterUrl = posterPath.toImageUrl(IMAGE_QUALITY_W500),
        backdropUrl = backdropPath.toImageUrl(IMAGE_QUALITY_W780),
        voteAverage = voteAverage ?: 0.0,
        releaseDate = releaseDate.orEmpty(),
        runtime = runtime,
        genres = genres?.map { it.name }.orEmpty()
    )
