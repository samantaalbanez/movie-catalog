package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.data.mapper.toDomain
import br.com.samantaalbanez.moviescatalog.data.service.MoviesService
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import javax.inject.Inject

internal class MoviesRepositoryImpl @Inject constructor(
    private val service: MoviesService
) : MoviesRepository {

    override suspend fun getMovies(page: Int): Result<List<Movie>> {
        return runCatching {
            val response = service.getMovies(page = page)
            response.results.map { it.toDomain() }
        }
    }
}