package br.com.samantaalbanez.moviescatalog.domain.usecase

import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import br.com.samantaalbanez.moviescatalog.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> =
        repository.getMovies()
}
