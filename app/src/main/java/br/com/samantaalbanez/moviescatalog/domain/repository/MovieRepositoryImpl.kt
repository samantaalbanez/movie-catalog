package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.data.mapper.toDomain
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService
) : MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> =
        service.getMovies(page = page).results.map { it.toDomain() }

    override suspend fun getTrendingMovies(page: Int): List<Movie> =
        service.getTrendingMovies(page = page).results.map { it.toDomain() }
}
