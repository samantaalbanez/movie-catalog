package br.com.samantaalbanez.moviescatalog.ui.home

internal sealed interface HomeUiEvent {
    data object Refresh : HomeUiEvent
    data object Retry : HomeUiEvent
    data class OnMovieClicked(val movieId: Int) : HomeUiEvent
}
