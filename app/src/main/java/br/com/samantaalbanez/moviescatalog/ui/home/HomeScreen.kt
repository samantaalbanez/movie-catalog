package br.com.samantaalbanez.moviescatalog.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.samantaalbanez.moviescatalog.ui.home.components.MovieCard
import br.com.samantaalbanez.moviescatalog.ui.home.components.HorizontalMovieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Ouve os efeitos colaterais de disparo único (Navigate e Toast)
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
            TopAppBar(
                title = {
                    Text(
                        text = "CineStream",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is HomeUiState.Success -> {
                    // PullToRefresh envoltório recebendo a flag diretamente de state.isRefreshing
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.onEvent(HomeUiEvent.Refresh) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // 1. HERO BANNER
                            state.bannerMovie?.let { movie ->
                                item(span = { GridItemSpan(2) }) {
                                    MovieCard (
                                        movie = movie,
                                        onClick = {  }
                                    )
                                }
                            }

                            // 2. SEÇÃO CARROSSEL "EM ALTA"
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
                                                key = { it.id }
                                            ) { movie ->
                                                HorizontalMovieCard(
                                                    movie = movie,
                                                    onMovieClick = { viewModel.onEvent(HomeUiEvent.OnMovieClicked(it)) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 3. TÍTULO "TODOS OS POPULARES"
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

                            // 4. GRADE RESTANTE (POPULARES)
                            items(
                                items = state.popularMovies,
                                key = { it.id }
                            ) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClick = {  }
                                )
                            }
                        }
                    }
                }

                is HomeUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onEvent(HomeUiEvent.Retry) }) {
                            Text("Tentar Novamente")
                        }
                    }
                }
            }
        }
    }
}
