package br.com.samantaalbanez.moviescatalog.ui.util

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val <T : Any> LazyPagingItems<T>.isInitialLoading: Boolean
    get() = loadState.refresh is LoadState.Loading && itemCount == 0

val <T : Any> LazyPagingItems<T>.isInitialError: Boolean
    get() = loadState.refresh is LoadState.Error && itemCount == 0

val <T : Any> LazyPagingItems<T>.initialErrorMessage: String?
    get() = (loadState.refresh as? LoadState.Error)?.error?.localizedMessage
