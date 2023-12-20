package com.example.onlinebookstore;

import com.example.onlinebookstore.controller.GenreController;
import com.example.onlinebookstore.entity.Genre;
import com.example.onlinebookstore.service.GenreService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenreControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;


    @Test
    void testGetAllGenres() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/genres";
        URI uri = new URI(baseUrl);

        // Perform GET request
        ResponseEntity<Genre[]> responseEntity = this.restTemplate.getForEntity(uri, Genre[].class);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    void testCreateGenre() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/genres";
        URI uri = new URI(baseUrl);

        // Create a new genre
        Genre newGenre = new Genre();
        newGenre.setName("New Genre");

        // Perform POST request
        ResponseEntity<Genre> responseEntity = this.restTemplate.postForEntity(uri, newGenre, Genre.class);

        // Verify the response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("New Genre", responseEntity.getBody().getName());
    }

    @Test
    void testDeleteGenreSuccess() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/genres";
        URI uri = new URI(baseUrl);

        // Create a new genre for testing deletion
        Genre newGenre = new Genre();
        newGenre.setName("Genre to Delete");

        ResponseEntity<Genre> postResponse = this.restTemplate.postForEntity(uri, newGenre, Genre.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        // Delete the genre
        URI deleteUri = UriComponentsBuilder.fromUri(uri)
                .pathSegment(postResponse.getBody().getId().toString())
                .build().toUri();

        ResponseEntity<Void> deleteResponse = this.restTemplate.exchange(deleteUri, HttpMethod.DELETE, null, Void.class);

        // Verify the response
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Verify the genre is no longer present
        ResponseEntity<Genre> getResponse = this.restTemplate.getForEntity(deleteUri, Genre.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
    @Test
    void testDeleteGenreNotFound() {
        // Arrange
        Long nonExistingGenreId = 999L;
        when(genreService.getGenreById(nonExistingGenreId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/api/genres/{id}", HttpMethod.DELETE, null, Void.class, nonExistingGenreId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(genreService, never()).deleteGenre(nonExistingGenreId);
    }
    @Test
    void testUpdateGenreSuccess() {
        // Arrange
        Long genreId = 1L;

        Genre existingGenre = new Genre();
        existingGenre.setId(genreId);
        existingGenre.setName("Fiction");

        Genre updatedGenre = new Genre();
        updatedGenre.setId(genreId);
        updatedGenre.setName("Updated Genre");

        when(genreService.getGenreById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreService.saveGenre(updatedGenre)).thenReturn(updatedGenre);

        // Act
        ResponseEntity<Genre> responseEntity = genreController.updateGenre(genreId, updatedGenre);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() != null && "Updated Genre".equals(responseEntity.getBody().getName()));

        verify(genreService, times(1)).getGenreById(genreId);
        verify(genreService, times(1)).saveGenre(updatedGenre);
    }

    @Test
    void testUpdateGenreNotFound() {
        // Arrange
        Long genreId = 1L;

        Genre updatedGenre = new Genre();
        updatedGenre.setId(genreId);
        updatedGenre.setName("Updated Genre");

        when(genreService.getGenreById(genreId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Genre> responseEntity = genreController.updateGenre(genreId, updatedGenre);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(genreService, times(1)).getGenreById(genreId);
        verify(genreService, never()).saveGenre(updatedGenre);
    }
}
