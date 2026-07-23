package br.com.samantaalbanez.moviescatalog.data.remote

import br.com.samantaalbanez.moviescatalog.data.remote.OkHttpClientFactory
import okhttp3.Interceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class OkHttpClientFactoryTest {

    @Test
    fun `create should set 30 second timeouts and add provided interceptors`() {
        // Given
        val dummyInterceptor1 = Interceptor { chain -> chain.proceed(chain.request()) }
        val dummyInterceptor2 = Interceptor { chain -> chain.proceed(chain.request()) }

        // When
        val client = OkHttpClientFactory.create(dummyInterceptor1, dummyInterceptor2)

        // Then
        assertEquals(30000, client.connectTimeoutMillis)
        assertEquals(30000, client.readTimeoutMillis)
        assertEquals(2, client.interceptors.size)
        assertTrue(client.interceptors.contains(dummyInterceptor1))
        assertTrue(client.interceptors.contains(dummyInterceptor2))
    }

    @Test
    fun `create should return client with empty interceptors list when none are passed`() {
        // When
        val client = OkHttpClientFactory.create()

        // Then
        assertTrue(client.interceptors.isEmpty())
        assertEquals(30000, client.connectTimeoutMillis)
        assertEquals(30000, client.readTimeoutMillis)
    }
}
