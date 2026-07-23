package br.com.samantaalbanez.moviescatalog.ui.details

import androidx.lifecycle.ViewModel
import br.com.samantaalbanez.moviescatalog.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

}
