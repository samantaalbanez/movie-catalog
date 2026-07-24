package br.com.samantaalbanez.moviescatalog.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

internal class GenericPagingSource<T : Any, DTO : Any>(
    private val apiCall: suspend (page: Int) -> DTO,
    private val extractData: (DTO) -> List<T>,
    private val getTotalPages: (DTO) -> Int
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val response = apiCall(page)
            val items = extractData(response)
            val totalPages = getTotalPages(response)
            val isLastPage = page >= totalPages || items.isEmpty()

            LoadResult.Page(
                data = items,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (isLastPage) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
