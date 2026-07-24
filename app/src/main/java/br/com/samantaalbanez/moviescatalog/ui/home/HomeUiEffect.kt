package br.com.samantaalbanez.moviescatalog.ui.home

sealed interface HomeUiEffect {

    data class ShowToast(val message: String) : HomeUiEffect
    data class NavigateToDetails(val movieId: Int) : HomeUiEffect
    data object RefreshPaging : HomeUiEffect
    data object RetryPaging : HomeUiEffect
}
