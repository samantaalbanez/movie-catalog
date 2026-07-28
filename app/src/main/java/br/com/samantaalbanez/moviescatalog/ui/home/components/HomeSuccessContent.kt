package br.com.samantaalbanez.moviescatalog.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.ui.home.HomeUiEvent
import br.com.samantaalbanez.moviescatalog.ui.util.isRefreshing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeSuccessContent(
    trendingMovies: LazyPagingItems<Movie>,
    popularMovies: LazyPagingItems<Movie>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRefreshing = trendingMovies.isRefreshing || popularMovies.isRefreshing
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            trendingMovies.refresh()
            popularMovies.refresh()
            onEvent(HomeUiEvent.Refresh)
        },
        state = pullToRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            trendingSection(
                trendingMovies = trendingMovies,
                onMovieClick = { onEvent(HomeUiEvent.OnMovieClicked(it)) }
            )

            popularSection(
                popularMovies = popularMovies,
                onMovieClick = { onEvent(HomeUiEvent.OnMovieClicked(it)) }
            )
        }
    }
}
