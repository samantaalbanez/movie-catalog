package br.com.samantaalbanez.moviescatalog.core.network

import br.com.samantaalbanez.moviescatalog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            .addHeader("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}