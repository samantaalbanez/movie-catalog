package br.com.samantaalbanez.moviescatalog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.samantaalbanez.moviescatalog.ui.details.MovieDetailsRoute
import br.com.samantaalbanez.moviescatalog.ui.home.HomeRoute // Sua HomeScreen / HomeRoute

@Composable
internal fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(
                onMovieClick = { movieId ->
                    navController.navigate("details/$movieId")
                }
            )
        }

        composable(
            route = "details/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            MovieDetailsRoute(
                viewModel = hiltViewModel(),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
