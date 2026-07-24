package br.com.samantaalbanez.moviescatalog.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.samantaalbanez.moviescatalog.data.mapper.toDomain
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import br.com.samantaalbanez.moviescatalog.domain.model.Movie

internal class MoviePagingSource(
    private val service: MovieService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val response = service.getMovies(page = page)
            val movies = response.results.map { it.toDomain() }
            val isLastPage = page >= response.totalPages || movies.isEmpty()

            LoadResult.Page(
                data = movies,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (isLastPage) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
