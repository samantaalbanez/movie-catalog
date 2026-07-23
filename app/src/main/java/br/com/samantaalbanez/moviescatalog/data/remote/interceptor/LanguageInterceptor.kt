package br.com.samantaalbanez.moviescatalog.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

internal class LanguageInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(LANGUAGE_PARAM, getDeviceLanguage())
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    private fun getDeviceLanguage(): String {
        val locale = Locale.getDefault()
        val language = locale.language
        val country = locale.country

        return if (country.isNotBlank()) {
            "$language-$country"
        } else {
            language
        }
    }

    companion object {
        private const val LANGUAGE_PARAM = "language"
    }
}
