package br.com.samantaalbanez.moviescatalog.data.remote

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

internal object OkHttpClientFactory {
    private const val TIMEOUT_SECONDS = 10L

    fun create(vararg interceptors: Interceptor, @ApplicationContext context: Context): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize)
        val builder = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        interceptors.forEach { interceptor ->
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }
}
