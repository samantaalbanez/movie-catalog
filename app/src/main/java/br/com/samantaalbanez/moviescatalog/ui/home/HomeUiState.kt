package br.com.samantaalbanez.moviescatalog.ui.home

import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Error(val message: String) : HomeUiState

    data class Success(
        val bannerMovie: Movie?,
        val trendingMovies: List<Movie> = emptyList(),
        val popularMovies: List<Movie> = emptyList(),
        val isRefreshing: Boolean = false
    ) : HomeUiState
}
