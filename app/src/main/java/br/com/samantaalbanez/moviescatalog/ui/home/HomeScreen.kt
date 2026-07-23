package br.com.samantaalbanez.moviescatalog.ui.home

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.samantaalbanez.moviescatalog.ui.home.components.HeroMovieCard
import br.com.samantaalbanez.moviescatalog.ui.home.components.HorizontalMovieCard
import br.com.samantaalbanez.moviescatalog.ui.home.components.MovieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

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
        // Componente de Atualização (Pull-to-Refresh)
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.loadPopularMovies(isRefresh = true) },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is HomeUiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is HomeUiState.Success -> {
                        val heroMovie = state.movies.firstOrNull()
                        val trendingMovies = state.movies.drop(1).take(6)
                        val allMovies = state.movies.drop(7)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // 1. HERO BANNER
                            if (heroMovie != null) {
                                item(span = { GridItemSpan(2) }) {
                                    HeroMovieCard(
                                        movie = heroMovie,
                                        onMovieClick = onMovieClick
                                    )
                                }
                            }

                            // 2. SESSÃO CARROSSEL "EM ALTA"
                            if (trendingMovies.isNotEmpty()) {
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
                                                items = trendingMovies,
                                                key = { it.id }
                                            ) { movie ->
                                                HorizontalMovieCard(
                                                    movie = movie,
                                                    onMovieClick = onMovieClick
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 3. TÍTULO "TODOS OS POPULARES"
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    text = "Todos os Populares",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            // 4. GRADE RESTANTE
                            items(
                                items = allMovies,
                                key = { it.id }
                            ) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onMovieClick = onMovieClick
                                )
                            }
                        }
                    }

                    is HomeUiState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadPopularMovies() }) {
                                Text("Tentar Novamente")
                            }
                        }
                    }
                }
            }
        }
    }
}