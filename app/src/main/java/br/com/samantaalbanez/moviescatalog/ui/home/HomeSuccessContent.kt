package br.com.samantaalbanez.moviescatalog.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.samantaalbanez.moviescatalog.ui.home.components.BannerMovieCard
import br.com.samantaalbanez.moviescatalog.ui.home.components.HorizontalMovieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeSuccessContent(
    state: HomeUiState.Success,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
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
            state.bannerMovie?.let { banner ->
                item(span = { GridItemSpan(2) }) {
                    BannerMovieCard (
                        movie = banner,
                        onClick = { movieId ->
                            onEvent(HomeUiEvent.OnMovieClicked(movieId))
                        }
                    )
                }
            }

            if (state.trendingMovies.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Em Alta Agora",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.trendingMovies,
                                key = { movie -> movie.id }
                            ) { movie ->
                                HorizontalMovieCard(
                                    movie = movie,
                                    onMovieClick = { movieId ->
                                        onEvent(HomeUiEvent.OnMovieClicked(movieId))
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (state.popularMovies.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Todos os Populares",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            items(
                items = state.popularMovies,
                key = { movie -> movie.id }
            ) { movie ->
                BannerMovieCard(
                    movie = movie,
                    onClick = {
                        onEvent(HomeUiEvent.OnMovieClicked(movie.id))
                    }
                )
            }
        }
    }
}