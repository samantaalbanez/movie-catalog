package br.com.samantaalbanez.moviescatalog.ui.details

internal sealed interface MovieDetailsUiEvent {
    data object Retry : MovieDetailsUiEvent
    data object OnBackClicked : MovieDetailsUiEvent
}
