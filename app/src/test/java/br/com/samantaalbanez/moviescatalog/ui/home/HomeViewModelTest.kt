package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetMoviesUseCase
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetTrendingMoviesUseCase
import br.com.samantaalbanez.moviescatalog.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val getMoviesUseCase: GetMoviesUseCase = mockk()
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        every { getMoviesUseCase() } returns flowOf(PagingData.empty())
        every { getTrendingMoviesUseCase() } returns flowOf(PagingData.empty())

        viewModel = HomeViewModel(
            getMoviesUseCase = getMoviesUseCase,
            getTrendingMoviesUseCase = getTrendingMoviesUseCase
        )
    }

    @Test
    fun `when OnMovieClicked event is sent, should emit NavigateToDetails effect`() = runTest {
        // Given
        val emittedEffects = mutableListOf<HomeUiEffect>()
        val movieId = 42

        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.collect { emittedEffects.add(it) }
        }

        // When
        viewModel.onEvent(HomeUiEvent.OnMovieClicked(movieId))

        // Then
        assertEquals(1, emittedEffects.size)
        assertEquals(HomeUiEffect.NavigateToDetails(movieId), emittedEffects.first())

        collectJob.cancel()
    }

    @Test
    fun `when Refresh event is sent, should emit RefreshPaging effect`() = runTest {
        // Given
        val emittedEffects = mutableListOf<HomeUiEffect>()

        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.collect { emittedEffects.add(it) }
        }

        // When
        viewModel.onEvent(HomeUiEvent.Refresh)

        // Then
        assertEquals(HomeUiEffect.RefreshPaging, emittedEffects.first())

        collectJob.cancel()
    }

    @Test
    fun `when onLoadStateChanged receives Error with items, should emit ShowToast effect`() = runTest {
        // Given
        val emittedEffects = mutableListOf<HomeUiEffect>()
        val errorMessage = "Não foi possível atualizar o catálogo."
        val errorLoadState = createCombinedLoadStates(
            refresh = LoadState.Error(RuntimeException(errorMessage))
        )

        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.collect { emittedEffects.add(it) }
        }

        // When
        viewModel.onLoadStateChanged(
            loadStates = errorLoadState,
            itemCount = 10
        )

        // Then
        assertEquals(HomeUiEffect.ShowToast(errorMessage), emittedEffects.first())

        collectJob.cancel()
    }

    private fun createCombinedLoadStates(
        refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
        prepend: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
        append: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
    ): CombinedLoadStates {
        return CombinedLoadStates(
            refresh = refresh,
            prepend = prepend,
            append = append,
            source = LoadStates(refresh = refresh, prepend = prepend, append = append)
        )
    }
}
