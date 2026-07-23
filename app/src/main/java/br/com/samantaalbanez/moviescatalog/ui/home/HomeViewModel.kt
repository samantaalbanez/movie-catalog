package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetMoviesUseCase
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

        private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        private val _uiEffect = Channel<HomeUiEffect>()
        val uiEffect = _uiEffect.receiveAsFlow()

        init {
            onEvent(HomeUiEvent.LoadData)
        }

        fun onEvent(event: HomeUiEvent) {
            when (event) {
                HomeUiEvent.LoadData -> loadHomeScreenData(isPullToRefresh = false)
                HomeUiEvent.Refresh -> loadHomeScreenData(isPullToRefresh = true)
                HomeUiEvent.Retry -> loadHomeScreenData(isPullToRefresh = false)
                is HomeUiEvent.OnMovieClicked -> navigateToDetails(event.movieId)
            }
        }

        private fun loadHomeScreenData(isPullToRefresh: Boolean) {
            viewModelScope.launch {
                val currentState = _uiState.value

                _uiState.value = if (isPullToRefresh && currentState is HomeUiState.Success) {
                    currentState.copy(isRefreshing = true)
                } else {
                    HomeUiState.Loading
                }

                fetchHomeScreenMovies().fold(
                    onSuccess = { (trendingMovies, popularMovies) ->
                        _uiState.value = HomeUiState.Success(
                            bannerMovie = trendingMovies.firstOrNull(),
                            trendingMovies = trendingMovies.drop(1),
                            popularMovies = popularMovies,
                            isRefreshing = false
                        )
                    },
                    onFailure = { throwable ->
                        handleError(throwable, isPullToRefresh, currentState)
                    }
                )
            }
        }

        private suspend fun fetchHomeScreenMovies(): Result<Pair<List<Movie>, List<Movie>>> = coroutineScope {
            val listOfMovies = async {
                getMoviesUseCase()
            }
            val trendingMovies = async { getTrendingMoviesUseCase() }

            val trendingMoviesResult = trendingMovies.await()
            val listOfMoviesResult = listOfMovies.await()

            trendingMoviesResult.fold(
                onSuccess = { trendingMovies ->
                    listOfMoviesResult.fold(
                        onSuccess = { movies ->
                            Result.success(Pair(trendingMovies, movies))
                        },
                        onFailure = { Result.failure(it) }
                    )
                },
                onFailure = { Result.failure(it) }
            )
        }

        private suspend fun handleError(
            throwable: Throwable,
            isPullToRefresh: Boolean,
            previousState: HomeUiState
        ) {
            if (isPullToRefresh && previousState is HomeUiState.Success) {
                _uiState.value = previousState.copy(isRefreshing = false)
                _uiEffect.send(
                    HomeUiEffect.ShowToast("Não foi possível atualizar o catálogo.")
                )
            } else {
                _uiState.value = HomeUiState.Error(
                    message = throwable.localizedMessage ?: "Erro ao carregar catálogo"
                )
            }
        }

    private fun navigateToDetails(movieId: Int) {
        viewModelScope.launch {
            _uiEffect.send(HomeUiEffect.NavigateToDetails(movieId))
        }
    }
}
