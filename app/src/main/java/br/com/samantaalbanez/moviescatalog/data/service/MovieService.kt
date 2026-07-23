package br.com.samantaalbanez.moviescatalog.data.service

import br.com.samantaalbanez.moviescatalog.data.remote.dto.MovieDetailsDto
import br.com.samantaalbanez.moviescatalog.data.remote.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MovieService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
    ): MoviesResponseDto

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1,
    ): MoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): MovieDetailsDto
}
