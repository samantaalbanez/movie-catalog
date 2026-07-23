package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.data.dto.MovieDetailsDto
import br.com.samantaalbanez.moviescatalog.data.dto.MovieDto
import br.com.samantaalbanez.moviescatalog.data.dto.MoviesResponseDto
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

internal class MovieRepositoryImplTest {

    private val service: MovieService = mockk()
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(service)
    }

    @Test
    fun `given page when getMovies is called then return mapped list of movies`() = runTest {
        // Given
        val page = 1
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            overview = "Overview test",
            posterPath = "/path.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.0,
            releaseDate = "2023-01-01"
        )
        val responseDto = MoviesResponseDto(results = listOf(movieDto), page = 1, totalPages = 4, totalResults = 400)

        coEvery { service.getMovies(page) } returns responseDto

        // When
        val result = repository.getMovies(page)

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test Movie", result[0].title)
        coVerify(exactly = 1) { service.getMovies(page) }
    }

    @Test
    fun `given page when getTrendingMovies is called then return mapped list of trending movies`() = runTest {
        // Given
        val page = 1
        val movieDto = MovieDto(
            id = 2,
            title = "Trending Movie",
            overview = "Trending overview",
            posterPath = "/trending.jpg",
            backdropPath = "/backdrop-trending.jpg",
            voteAverage = 9.0,
            releaseDate = "2023-05-01"
        )
        val responseDto = MoviesResponseDto(results = listOf(movieDto), page = 4, totalResults = 400, totalPages = 4)

        coEvery { service.getTrendingMovies(page) } returns responseDto

        // When
        val result = repository.getTrendingMovies(page)

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(2, result[0].id)
        assertEquals("Trending Movie", result[0].title)
        coVerify(exactly = 1) { service.getTrendingMovies(page) }
    }

    @Test
    fun `given movieId when getMovieDetails is called then return mapped movie details`() = runTest {
        // Given
        val movieId = 100
        val detailsDto = MovieDetailsDto(
            id = movieId,
            title = "Detail Movie",
            overview = "Detail overview",
            posterPath = "/detail.jpg",
            backdropPath = "/backdrop-detail.jpg",
            voteAverage = 7.5,
            releaseDate = "2023-10-10",
            runtime = 120,
            genres = emptyList()
        )

        coEvery { service.getMovieDetails(movieId) } returns detailsDto

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertNotNull(result)
        assertEquals(movieId, result.id)
        assertEquals("Detail Movie", result.title)
        coVerify(exactly = 1) { service.getMovieDetails(movieId) }
    }
}