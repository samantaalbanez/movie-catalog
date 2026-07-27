package br.com.samantaalbanez.moviescatalog.data.remote

import android.content.Context
import br.com.samantaalbanez.moviescatalog.data.remote.OkHttpClientFactory
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

internal class OkHttpClientFactoryTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mockk(relaxed = true) {
            every { cacheDir } returns File(System.getProperty("java.io.tmpdir"), "http_cache")
        }
    }

    @Test
    fun `create should set 30 second timeouts and add provided interceptors`() {
        // Given
        val dummyInterceptor1 = Interceptor { chain -> chain.proceed(chain.request()) }
        val dummyInterceptor2 = Interceptor { chain -> chain.proceed(chain.request()) }

        // When
        val client = OkHttpClientFactory.create(interceptors = arrayOf(dummyInterceptor1, dummyInterceptor2), context = context)

        // Then
        assertEquals(10000, client.connectTimeoutMillis)
        assertEquals(10000, client.readTimeoutMillis)
        assertEquals(2, client.interceptors.size)
        assertTrue(client.interceptors.contains(dummyInterceptor1))
        assertTrue(client.interceptors.contains(dummyInterceptor2))
    }

    @Test
    fun `create should return client with empty interceptors list when none are passed`() {
        // When
        val client = OkHttpClientFactory.create(context = context)

        // Then
        assertTrue(client.interceptors.isEmpty())
        assertEquals(10000, client.connectTimeoutMillis)
        assertEquals(10000, client.readTimeoutMillis)
    }
}
