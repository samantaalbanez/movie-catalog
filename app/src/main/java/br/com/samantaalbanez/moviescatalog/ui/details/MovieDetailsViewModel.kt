package br.com.samantaalbanez.moviescatalog.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "movieId não foi fornecido para a DetailsViewModel"
    }

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<MovieDetailsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadMovieDetails()
    }

    fun onEvent(event: MovieDetailsUiEvent) {
        when (event) {
            is MovieDetailsUiEvent.Retry -> loadMovieDetails()
            is MovieDetailsUiEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _uiEffect.send(MovieDetailsUiEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading
            getMovieDetailsUseCase(movieId).fold(
                onSuccess = { movie ->
                    _uiState.value = MovieDetailsUiState.Success(movie)
                },
                onFailure = { throwable ->
                    _uiState.value = MovieDetailsUiState.Error(
                        message = throwable.localizedMessage ?: "Erro ao carregar os detalhes do filme."
                    )
                }
            )
        }
    }
}
