package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetMoviesUseCase
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    val trendingMoviesPagingFlow: Flow<PagingData<Movie>> = getTrendingMoviesUseCase()
        .cachedIn(viewModelScope)

    val moviesPagingFlow: Flow<PagingData<Movie>> = getMoviesUseCase()
        .cachedIn(viewModelScope)

    private val _uiEffect = Channel<HomeUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> sendEffect(HomeUiEffect.RefreshPaging)
            HomeUiEvent.Retry -> sendEffect(HomeUiEffect.RetryPaging)
            is HomeUiEvent.OnMovieClicked -> navigateToDetails(event.movieId)
        }
    }

    fun onLoadStateChanged(loadStates: CombinedLoadStates, itemCount: Int) {
        val isAppendError = loadStates.append is LoadState.Error
        val isRefreshErrorWithItems = loadStates.refresh is LoadState.Error && itemCount > 0

        if (isAppendError) {
            sendEffect(HomeUiEffect.ShowToast("Falha ao carregar mais filmes."))
        } else if (isRefreshErrorWithItems) {
            sendEffect(HomeUiEffect.ShowToast("Não foi possível atualizar o catálogo."))
        }
    }

    private fun navigateToDetails(movieId: Int) {
        sendEffect(HomeUiEffect.NavigateToDetails(movieId))
    }

    private fun sendEffect(effect: HomeUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
