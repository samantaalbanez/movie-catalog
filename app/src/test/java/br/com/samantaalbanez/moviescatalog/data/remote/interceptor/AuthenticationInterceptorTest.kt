package br.com.samantaalbanez.moviescatalog.data.remote.interceptor

import br.com.samantaalbanez.moviescatalog.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class AuthenticationInterceptorTest {

    private val interceptor = AuthenticationInterceptor()

    @Test
    fun `given request intercepted, then add authorization header`() {
        // Given
        val initialRequest = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular")
            .build()

        val chain: Interceptor.Chain = mockk()
        val mockResponse: Response = mockk()
        val requestSlot = slot<Request>()

        every { chain.request() } returns initialRequest
        every { chain.proceed(capture(requestSlot)) } returns mockResponse

        // When
        interceptor.intercept(chain)

        // Then
        val modifiedRequest = requestSlot.captured
        val authHeader = modifiedRequest.header("Authorization")

        assertTrue(authHeader?.startsWith("Bearer ") == true)
        assertEquals("Bearer ${BuildConfig.API_TOKEN}", authHeader)
        assertEquals("application/json", modifiedRequest.header("accept"))
    }

    @Test
    fun `given request with headers, intercept should preserve original headers`() {
        // Given
        val initialRequest = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular")
            .addHeader("Language", "PT-Br")
            .build()

        val chain: Interceptor.Chain = mockk()
        val mockResponse: Response = mockk()
        val requestSlot = slot<Request>()

        every { chain.request() } returns initialRequest
        every { chain.proceed(capture(requestSlot)) } returns mockResponse

        // When
        interceptor.intercept(chain)

        // Then
        val modifiedRequest = requestSlot.captured
        val authHeader = modifiedRequest.header("Authorization")

        assertEquals("PT-Br", modifiedRequest.header("Language"))
        assertTrue(authHeader?.startsWith("Bearer ") == true)
        assertEquals("Bearer ${BuildConfig.API_TOKEN}", authHeader)
        assertEquals("application/json", modifiedRequest.header("accept"))
    }
}
