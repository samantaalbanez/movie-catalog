package br.com.samantaalbanez.moviescatalog.ui.details

internal sealed interface MovieDetailsUiEffect {
    data object NavigateBack : MovieDetailsUiEffect
}