package br.com.samantaalbanez.moviescatalog.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.samantaalbanez.moviescatalog.ui.components.TopAppBar
import br.com.samantaalbanez.moviescatalog.ui.home.components.ErrorContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToDetails -> onMovieClick(effect.movieId)
                is HomeUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = "Movies :)")
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading ->
                    CircularProgressIndicator()

                is HomeUiState.Success ->
                    HomeSuccessContent(
                        state = state,
                        onEvent = viewModel::onEvent
                    )

                is HomeUiState.Error ->
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.onEvent(HomeUiEvent.Retry) }
                    )
            }
        }
    }
}
