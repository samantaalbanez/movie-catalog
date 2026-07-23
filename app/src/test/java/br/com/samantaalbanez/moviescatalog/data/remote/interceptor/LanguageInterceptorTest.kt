package br.com.samantaalbanez.moviescatalog.data.remote.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class LanguageInterceptorTest {

    private val interceptor = LanguageInterceptor()
    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `given language and country exist, intercept should add language param`() {
        // Given
        Locale.setDefault(Locale("pt", "BR"))

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
        val modifiedRequest = requestSlot.captured
        val languageParam = modifiedRequest.url.queryParameter("language")

        // Then
        assertEquals("pt-BR", languageParam)
    }

    @Test
    fun `given country is blank, intercept should add language code only`() {
        // Given
        Locale.setDefault(Locale("en", ""))

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
        val languageParam = modifiedRequest.url.queryParameter("language")

        assertEquals("en", languageParam)
    }

    @Test
    fun `given request with headers, intercept should preserve original headers`() {
        // Given
        Locale.setDefault(Locale("es", "ES"))

        val initialRequest = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular?page=1&sort_by=popularity.desc")
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

        assertEquals("1", modifiedRequest.url.queryParameter("page"))
        assertEquals("popularity.desc", modifiedRequest.url.queryParameter("sort_by"))
        assertEquals("es-ES", modifiedRequest.url.queryParameter("language"))
    }
}
