package br.com.samantaalbanez.moviescatalog.data.service

import br.com.samantaalbanez.moviescatalog.data.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MoviesService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "pt-BR"
    ): MoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "pt-BR"
    ): MoviesResponseDto
}
