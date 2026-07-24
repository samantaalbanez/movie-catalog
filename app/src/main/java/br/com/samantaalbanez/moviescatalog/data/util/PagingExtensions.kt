package br.com.samantaalbanez.moviescatalog.data.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.samantaalbanez.moviescatalog.data.mapper.toDomain
import br.com.samantaalbanez.moviescatalog.data.remote.dto.MoviesResponseDto
import br.com.samantaalbanez.moviescatalog.data.remote.paging.GenericPagingSource
import br.com.samantaalbanez.moviescatalog.domain.model.Movie
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_PAGE_SIZE = 20

internal fun <T : Any, DTO : Any> createPager(
    pageSize: Int = DEFAULT_PAGE_SIZE,
    apiCall: suspend (page: Int) -> DTO,
    extractData: (DTO) -> List<T>,
    getTotalPages: (DTO) -> Int
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            GenericPagingSource(
                apiCall = apiCall,
                extractData = extractData,
                getTotalPages = getTotalPages
            )
        }
    ).flow
}

internal fun createMoviePager(
    apiCall: suspend (page: Int) -> MoviesResponseDto
): Flow<PagingData<Movie>> = createPager(
    apiCall = apiCall,
    extractData = { response -> response.results.map { it.toDomain() } },
    getTotalPages = { response -> response.totalPages }
)
