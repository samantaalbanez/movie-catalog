package br.com.samantaalbanez.moviescatalog.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.samantaalbanez.moviescatalog.R
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.ui.components.ErrorScreen
import br.com.samantaalbanez.moviescatalog.ui.components.TopAppBar
import br.com.samantaalbanez.moviescatalog.ui.home.components.HomeSuccessContent
import br.com.samantaalbanez.moviescatalog.ui.home.components.skeleton.HomeSkeletonContent
import br.com.samantaalbanez.moviescatalog.ui.util.isInitialError
import br.com.samantaalbanez.moviescatalog.ui.util.isInitialLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit = {}
) {
    val trendingMovies = viewModel.trendingMoviesPagingFlow.collectAsLazyPagingItems()
    val popularMovies = viewModel.moviesPagingFlow.collectAsLazyPagingItems()

    ObserveLoadState(viewModel = viewModel, popularMovies = popularMovies)
    ObserveUiEffects(viewModel = viewModel, trendingMovies = trendingMovies, popularMovies = popularMovies, onMovieClick = onMovieClick)

    Scaffold(
        topBar = { TopAppBar(title = stringResource(R.string.title_app)) }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            HomeContentState(
                popularMovies = popularMovies,
                trendingMovies = trendingMovies,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ObserveLoadState(
    viewModel: HomeViewModel,
    popularMovies: LazyPagingItems<Movie>
) {
    LaunchedEffect(popularMovies.loadState) {
        viewModel.onLoadStateChanged(
            loadStates = popularMovies.loadState,
            itemCount = popularMovies.itemCount
        )
    }
}

@Composable
private fun ObserveUiEffects(
    viewModel: HomeViewModel,
    trendingMovies: LazyPagingItems<Movie>,
    popularMovies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToDetails -> onMovieClick(effect.movieId)
                is HomeUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                HomeUiEffect.RefreshPaging -> {
                    trendingMovies.refresh()
                    popularMovies.refresh()
                }
                HomeUiEffect.RetryPaging -> {
                    trendingMovies.retry()
                    popularMovies.retry()
                }
            }
        }
    }
}

@Composable
private fun HomeContentState(
    popularMovies: LazyPagingItems<Movie>,
    trendingMovies: LazyPagingItems<Movie>,
    viewModel: HomeViewModel
) {
    when {
        popularMovies.isInitialLoading -> {
            HomeSkeletonContent()
        }

        popularMovies.isInitialError -> {
            val errorState = popularMovies.loadState.refresh as LoadState.Error
            ErrorScreen(
                message = errorState.error.localizedMessage ?: stringResource(R.string.title_app),
                onRetry = { viewModel.onEvent(HomeUiEvent.Retry) }
            )
        }

        else -> {
            HomeSuccessContent(
                trendingMovies = trendingMovies,
                popularMovies = popularMovies,
                onEvent = viewModel::onEvent
            )
        }
    }
}
