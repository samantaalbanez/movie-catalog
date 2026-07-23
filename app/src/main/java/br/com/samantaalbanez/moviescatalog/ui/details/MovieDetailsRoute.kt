package br.com.samantaalbanez.moviescatalog.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun MovieDetailsRoute(
    viewModel: MovieDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is MovieDetailsUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    DetailsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
