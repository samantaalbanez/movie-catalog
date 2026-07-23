package br.com.samantaalbanez.moviescatalog.domain.usecase

import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class GetMovieDetailsUseCaseTest {

    private val repository: MovieRepository = mockk()
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `given movieId when invoke is called then return success result with movie details`() = runTest {
        // Given
        val movieId = 100
        val expectedMovie = Movie(
            id = movieId,
            title = "Inception",
            overview = "A thief who steals corporate secrets...",
            posterUrl = "/poster.jpg",
            backdropUrl = "/backdrop.jpg",
            voteAverage = 8.8,
            releaseDate = "2010-07-16"
        )
        coEvery { repository.getMovieDetails(movieId) } returns expectedMovie

        // When
        val result = useCase(id = movieId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedMovie, result.getOrNull())
        coVerify(exactly = 1) { repository.getMovieDetails(movieId) }
    }

    @Test
    fun `given repository throws exception when invoke is called then return failure result`() = runTest {
        // Given
        val movieId = 100
        val expectedException = RuntimeException("Network error")
        coEvery { repository.getMovieDetails(movieId) } throws expectedException

        // When
        val result = useCase(id = movieId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.getMovieDetails(movieId) }
    }
}
