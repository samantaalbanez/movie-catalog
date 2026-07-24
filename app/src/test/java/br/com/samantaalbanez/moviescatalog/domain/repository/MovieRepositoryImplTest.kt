package br.com.samantaalbanez.moviescatalog.domain.repository

import br.com.samantaalbanez.moviescatalog.data.remote.dto.MovieDetailsDto
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

internal class MovieRepositoryImplTest {

    private val service: MovieService = mockk()
    private lateinit var repository: MovieRepository

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(service = service)
    }

    @Test
    fun `getMovies should return flow of paging data`() = runTest {
        // When
        val flow = repository.getMovies()
        val result = flow.first()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `getTrendingMovies should return flow of paging data`() = runTest {
        // When
        val flow = repository.getTrendingMovies()
        val result = flow.first()

        // Then
        assertNotNull(result)
    }

    @Test
    fun `getMovieDetails should call service and return mapped movie`() = runTest {
        // Given
        val movieId = 123
        val mockResponse = mockk<MovieDetailsDto>(relaxed = true) {
            coEvery { id } returns movieId
            coEvery { title } returns "Movie Title"
        }

        coEvery { service.getMovieDetails(movieId) } returns mockResponse

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        coVerify(exactly = 1) { service.getMovieDetails(movieId) }
    }
}
