package br.com.samantaalbanez.moviescatalog.domain.usecase

import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

internal class GetTrendingMoviesUseCaseTest {

    private val repository: MovieRepository = mockk()
    private val useCase = GetTrendingMoviesUseCase(repository)

    @Test
    fun `invoke should return flow of paging data from repository`() = runTest {
        // Given
        val dummyPagingData = PagingData.from(emptyList<Movie>())
        every { repository.getTrendingMovies() } returns flowOf(dummyPagingData)

        // When
        val result = useCase().first()

        // Then
        assertNotNull(result)
        verify(exactly = 1) { repository.getTrendingMovies() }
    }
}
