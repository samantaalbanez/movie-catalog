package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal interface MoviesRepository {
    suspend fun getMovies(page: Int = 1): Result<List<Movie>>
}
