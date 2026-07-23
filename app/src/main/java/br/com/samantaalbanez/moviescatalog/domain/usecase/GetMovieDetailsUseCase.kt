package br.com.samantaalbanez.moviescatalog.domain.usecase

import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.repository.MovieRepository
import javax.inject.Inject

internal class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Result<Movie> = runCatching {
        repository.getMovieDetails(movieId = id)
    }
}
