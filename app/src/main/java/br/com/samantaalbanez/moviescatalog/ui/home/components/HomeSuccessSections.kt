package br.com.samantaalbanez.moviescatalog.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

internal fun LazyGridScope.trendingSection(
    trendingMovies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit
) {
    if (trendingMovies.itemCount > 0) {
        trendingMovies[0]?.let { banner ->
            item(span = { GridItemSpan(2) }) {
                BannerMovieCard(movie = banner, onClick = onMovieClick)
            }
        }
    }

    if (trendingMovies.itemCount > 1) {
        item(span = { GridItemSpan(2) }) {
            TrendingMoviesRow(
                trendingMovies = trendingMovies,
                onMovieClick = onMovieClick
            )
        }
    }
}

internal fun LazyGridScope.popularSection(
    popularMovies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit
) {
    if (popularMovies.itemCount > 0) {
        item(span = { GridItemSpan(2) }) {
            SectionTitle(
                title = stringResource(R.string.home_popular_section),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    items(
        count = popularMovies.itemCount,
        key = popularMovies.itemKey { it.id }
    ) { index ->
        popularMovies[index]?.let { movie ->
            BannerMovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
        }
    }

    if (popularMovies.loadState.append is LoadState.Loading) {
        item(span = { GridItemSpan(2) }) {
            LoadingItem()
        }
    }
}

@Composable
internal fun TrendingMoviesRow(
    trendingMovies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        SectionTitle(title = stringResource(R.string.home_trending_section))
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(
                count = trendingMovies.itemCount - 1,
                key = { index -> trendingMovies.peek(index + 1)?.id ?: "trending_$index" }
            ) { index ->
                trendingMovies[index + 1]?.let { movie ->
                    HorizontalMovieCard(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}

@Composable
internal fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
internal fun LoadingItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
