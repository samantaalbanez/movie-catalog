package br.com.samantaalbanez.moviescatalog.ui.details

import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal sealed interface MovieDetailsUiState {
    data object Loading : MovieDetailsUiState
    data class Success(val movie: Movie) : MovieDetailsUiState
    data class Error(val message: String) : MovieDetailsUiState
}
