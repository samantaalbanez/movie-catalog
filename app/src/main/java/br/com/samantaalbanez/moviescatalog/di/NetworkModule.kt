package br.com.samantaalbanez.moviescatalog.di

import br.com.samantaalbanez.moviescatalog.BuildConfig
import br.com.samantaalbanez.moviescatalog.data.remote.interceptor.AuthenticationInterceptor
import br.com.samantaalbanez.moviescatalog.data.remote.interceptor.LanguageInterceptor
import br.com.samantaalbanez.moviescatalog.data.remote.GsonFactory
import br.com.samantaalbanez.moviescatalog.data.remote.OkHttpClientFactory
import br.com.samantaalbanez.moviescatalog.data.service.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(): AuthenticationInterceptor =
        AuthenticationInterceptor()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthenticationInterceptor,
        languageInterceptor: LanguageInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClientFactory.create(authInterceptor, loggingInterceptor, languageInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.create()))
            .build()

    @Provides
    @Singleton
    fun provideMoviesService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}
