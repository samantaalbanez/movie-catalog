package br.com.samantaalbanez.moviescatalog.domain.repository

import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.data.mapper.detailsToDomain
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import br.com.samantaalbanez.moviescatalog.data.util.createMoviePager
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val service: MovieService
) : MovieRepository {

    override fun getMovies(): Flow<PagingData<Movie>> = createMoviePager { page ->
        service.getMovies(page = page)
    }

    override fun getTrendingMovies(): Flow<PagingData<Movie>> = createMoviePager { page ->
        service.getTrendingMovies(page = page)
    }

    override suspend fun getMovieDetails(movieId: Int): Movie =
        service.getMovieDetails(movieId).detailsToDomain()
}
