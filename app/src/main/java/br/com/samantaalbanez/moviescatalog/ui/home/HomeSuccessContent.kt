package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import br.com.samantaalbanez.moviescatalog.R
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.ui.home.components.BannerMovieCard
import br.com.samantaalbanez.moviescatalog.ui.home.components.HorizontalMovieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeSuccessContent(
    trendingMovies: LazyPagingItems<Movie>,
    popularMovies: LazyPagingItems<Movie>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRefreshing = popularMovies.loadState.refresh is LoadState.Loading && popularMovies.itemCount > 0

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onEvent(HomeUiEvent.Refresh) },
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (trendingMovies.itemCount > 0) {
                trendingMovies[0]?.let { banner ->
                    item(span = { GridItemSpan(2) }) {
                        BannerMovieCard(
                            movie = banner,
                            onClick = { movieId ->
                                onEvent(HomeUiEvent.OnMovieClicked(movieId))
                            }
                        )
                    }
                }
            }

            if (trendingMovies.itemCount > 1) {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.home_trending_section),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                count = trendingMovies.itemCount - 1,
                                key = { index ->
                                    trendingMovies.peek(index + 1)?.id ?: index
                                }
                            ) { index ->
                                val movie = trendingMovies[index + 1]
                                movie?.let {
                                    HorizontalMovieCard(
                                        movie = it,
                                        onMovieClick = { movieId ->
                                            onEvent(HomeUiEvent.OnMovieClicked(movieId))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (popularMovies.itemCount > 0) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = stringResource(R.string.home_popular_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            items(
                count = popularMovies.itemCount,
                key = popularMovies.itemKey { movie -> movie.id }
            ) { index ->
                val movie = popularMovies[index]
                movie?.let {
                    BannerMovieCard(
                        movie = it,
                        onClick = {
                            onEvent(HomeUiEvent.OnMovieClicked(movie.id))
                        }
                    )
                }
            }

            if (popularMovies.loadState.append is LoadState.Loading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
