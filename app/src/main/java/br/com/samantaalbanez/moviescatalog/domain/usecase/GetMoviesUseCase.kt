package br.com.samantaalbanez.moviescatalog.domain.usecase

import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.repository.MoviesRepository
import javax.inject.Inject

internal class GetMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<List<Movie>> {
        return repository.getMovies(page)
    }
}
