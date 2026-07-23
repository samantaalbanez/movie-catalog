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

internal class GetMoviesUseCaseTest {

    private val repository: MovieRepository = mockk()
    private lateinit var useCase: GetMoviesUseCase

    @Before
    fun setUp() {
        useCase = GetMoviesUseCase(repository)
    }

    @Test
    fun `given page parameter when invoke is called then return success result with movies list`() = runTest {
        // Given
        val page = 1
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "The Matrix",
                overview = "Welcome to the Real World.",
                posterUrl = "/matrix.jpg",
                backdropUrl = "/matrix-backdrop.jpg",
                voteAverage = 8.7,
                releaseDate = "1999-03-31"
            )
        )
        coEvery { repository.getMovies(page) } returns expectedMovies

        // When
        val result = useCase(page = page)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedMovies, result.getOrNull())
        coVerify(exactly = 1) { repository.getMovies(page) }
    }

    @Test
    fun `given repository throws exception when invoke is called then return failure result`() = runTest {
        // Given
        val page = 1
        val expectedException = RuntimeException("Failed to fetch movies")
        coEvery { repository.getMovies(page) } throws expectedException

        // When
        val result = useCase(page = page)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.getMovies(page) }
    }
}
