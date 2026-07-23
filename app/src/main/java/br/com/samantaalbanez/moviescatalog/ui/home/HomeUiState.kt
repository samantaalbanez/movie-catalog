package br.com.samantaalbanez.moviescatalog.ui.home

import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val movies: List<Movie>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
