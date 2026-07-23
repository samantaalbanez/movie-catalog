package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal interface MovieRepository {
    suspend fun getMovies(page: Int = 1): List<Movie>
    suspend fun getTrendingMovies(page: Int = 1): List<Movie>
    suspend fun getMovieDetails(movieId: Int): Movie
}
