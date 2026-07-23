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

internal class GetTrendingMoviesUseCaseTest {

    private val repository: MovieRepository = mockk()
    private lateinit var useCase: GetTrendingMoviesUseCase

    @Before
    fun setUp() {
        useCase = GetTrendingMoviesUseCase(repository)
    }

    @Test
    fun `given page parameter when invoke is called then return success result with trending movies list`() = runTest {
        // Given
        val page = 1
        val expectedMovies = listOf(
            Movie(
                id = 10,
                title = "Oppenheimer",
                overview = "The story of J. Robert Oppenheimer...",
                posterUrl = "/oppenheimer.jpg",
                backdropUrl = "/oppenheimer-backdrop.jpg",
                voteAverage = 8.9,
                releaseDate = "2023-07-21"
            )
        )
        coEvery { repository.getTrendingMovies(page) } returns expectedMovies

        // When
        val result = useCase(page = page)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedMovies, result.getOrNull())
        coVerify(exactly = 1) { repository.getTrendingMovies(page) }
    }

    @Test
    fun `given repository throws exception when invoke is called then return failure result`() = runTest {
        // Given
        val page = 1
        val expectedException = RuntimeException("Failed to fetch trending movies")
        coEvery { repository.getTrendingMovies(page) } throws expectedException

        // When
        val result = useCase(page = page)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        coVerify(exactly = 1) { repository.getTrendingMovies(page) }
    }
}
