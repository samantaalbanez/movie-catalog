package br.com.samantaalbanez.moviescatalog.data.utils

import br.com.samantaalbanez.moviescatalog.data.util.Constants
import br.com.samantaalbanez.moviescatalog.data.util.toImageUrl
import org.junit.Assert.assertEquals
import org.junit.Test

internal class StringExtensionsTest {

    @Test
    fun `given a imagePath valid toImageUrl should format image URL correctly with default quality w500`() {
        // Given
        val imagePath = "/poster.jpg"
        val expectedUrl = "${Constants.BASE_IMAGE_URL}/w500/poster.jpg"

        // When
        val result = imagePath.toImageUrl()

        // Then
        assertEquals(expectedUrl, result)
    }

    @Test
    fun `given a imagePath toImageUrl should format image URL correctly with custom quality`() {
        // Given
        val imagePath = "/backdrop.jpg"
        val customQuality = "w780"
        val expectedUrl = "${Constants.BASE_IMAGE_URL}/w780/backdrop.jpg"

        // When
        val result = imagePath.toImageUrl(quality = customQuality)

        // Then
        assertEquals(expectedUrl, result)
    }

    @Test
    fun `given a imagePath null toImageUrl should return empty string`() {
        // Given
        val imagePath: String? = null

        // When
        val result = imagePath.toImageUrl()

        // Then
        assertEquals("", result)
    }
}
