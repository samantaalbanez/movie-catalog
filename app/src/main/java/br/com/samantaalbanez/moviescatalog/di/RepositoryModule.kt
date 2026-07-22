package br.com.samantaalbanez.moviescatalog.di

import br.com.samantaalbanez.moviescatalog.domain.repository.MoviesRepository
import br.com.samantaalbanez.moviescatalog.domain.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}
