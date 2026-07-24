package br.com.samantaalbanez.moviescatalog.domain.repository

import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import kotlinx.coroutines.flow.Flow

internal interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    fun getTrendingMovies(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Movie
}
