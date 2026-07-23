package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToDetails -> onMovieClick(effect.movieId)
                else -> Unit
            }
        }
    }

    HomeScreen(
        onMovieClick = onMovieClick
    )
}