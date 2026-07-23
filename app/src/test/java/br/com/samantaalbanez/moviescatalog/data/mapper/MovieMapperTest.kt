package br.com.samantaalbanez.moviescatalog.data.mapper

import br.com.samantaalbanez.moviescatalog.data.remote.dto.GenreDto
import br.com.samantaalbanez.moviescatalog.data.remote.dto.MovieDetailsDto
import br.com.samantaalbanez.moviescatalog.data.remote.dto.MovieDto
import org.junit.Assert.assertEquals
import org.junit.Test

internal class MovieMapperTest {

    @Test
    fun `given MovieDto when map then returns Model`() {
        // Given
        val movieDto = MovieDto(
            id = 100,
            title = "Inception",
            overview = "A thief who steals corporate secrets...",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 8.8,
            releaseDate = "2010-07-16",
        )

        // When
        val result = movieDto.toDomain()

        // Then
        assertEquals(100, result.id)
        assertEquals("Inception", result.title)
        assertEquals("A thief who steals corporate secrets...", result.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result.backdropUrl)
        assertEquals(8.8, result.voteAverage, 0.01)
        assertEquals("2010-07-16", result.releaseDate)

    }

    @Test
    fun `given MovieDetailsDto when map then returns Model`() {
        // Given
        val dto = MovieDetailsDto(
            id = 200,
            title = "Filme Sem Dados",
            overview = "test",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            voteAverage = 5.0,
            releaseDate = "2026-01-40",
            runtime = 10,
            genres = listOf(
                GenreDto(
                    id = 1234,
                    name = "name"
                )
            )
        )

        // When
        val result = dto.detailsToDomain()

        // Then
        assertEquals(200, result.id)
        assertEquals("Filme Sem Dados", result.title)
        assertEquals("test", result.overview)
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result.backdropUrl)
        assertEquals(5.0, result.voteAverage, 0.0)
        assertEquals("2026-01-40", result.releaseDate)
        assertEquals(10, result.runtime)
        assertEquals(listOf("name"), result.genres)
    }
}
