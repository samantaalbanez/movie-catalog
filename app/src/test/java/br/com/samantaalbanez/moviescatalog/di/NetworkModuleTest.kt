package br.com.samantaalbanez.moviescatalog.di

import br.com.samantaalbanez.moviescatalog.BuildConfig
import br.com.samantaalbanez.moviescatalog.data.remote.interceptor.AuthenticationInterceptor
import br.com.samantaalbanez.moviescatalog.data.remote.interceptor.LanguageInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit

internal class NetworkModuleTest {

    @Test
    fun `given required interceptors when provideOkHttpClient is called then return client containing all interceptors`() {
        // Given
        val authInterceptor = AuthenticationInterceptor()
        val languageInterceptor = LanguageInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()

        // When
        val result = NetworkModule.provideOkHttpClient(
            authInterceptor = authInterceptor,
            languageInterceptor = languageInterceptor,
            loggingInterceptor = loggingInterceptor
        )

        // Then
        assertNotNull(result)
        assertEquals(3, result.interceptors.size)
        assertTrue(result.interceptors.contains(authInterceptor))
        assertTrue(result.interceptors.contains(loggingInterceptor))
        assertTrue(result.interceptors.contains(languageInterceptor))
    }

    @Test
    fun `provideRetrofit should return Retrofit instance configured with base URL and OkHttpClient`() {
        // Given
        val okHttpClient = OkHttpClient.Builder().build()

        // When
        val result = NetworkModule.provideRetrofit(okHttpClient)

        // Then
        assertNotNull(result)
        assertEquals(BuildConfig.BASE_URL, result.baseUrl().toString())
        assertEquals(okHttpClient, result.callFactory())
    }

    @Test
    fun `provideMoviesService should return non-null MovieService implementation`() {
        // Given
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .build()

        // When
        val result = NetworkModule.provideMoviesService(retrofit)

        // Then
        assertNotNull(result)
    }
}