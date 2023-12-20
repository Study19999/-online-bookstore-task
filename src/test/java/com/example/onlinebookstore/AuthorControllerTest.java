package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Author;
import com.example.onlinebookstore.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private AuthorService authorService;


    @Test
    void testCreateAuthor() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/authors";
        URI uri = new URI(baseUrl);

        // Create a new author
        Author newAuthor = new Author();
        newAuthor.setName("New Author");

        // Perform POST request
        ResponseEntity<Author> responseEntity = this.restTemplate.postForEntity(uri, newAuthor, Author.class);

        // Verify the response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("New Author", responseEntity.getBody().getName());
    }

    @Test
    void testDeleteAuthor() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/authors";
        URI uri = new URI(baseUrl);

        // Create a new author for testing deletion
        Author newAuthor = new Author();
        newAuthor.setName("Author to Delete");

        ResponseEntity<Author> postResponse = this.restTemplate.postForEntity(uri, newAuthor, Author.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        // Delete the author
        URI deleteUri = UriComponentsBuilder.fromUri(uri)
                .pathSegment(postResponse.getBody().getId().toString())
                .build().toUri();

        ResponseEntity<Void> deleteResponse = this.restTemplate.exchange(deleteUri, HttpMethod.DELETE, null, Void.class);

        // Verify the response
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Verify the author is no longer present
        ResponseEntity<Author> getResponse = this.restTemplate.getForEntity(deleteUri, Author.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
    @Test
    void testDeleteAuthorNotFound() {
        // Arrange
        Long nonExistingAuthorId = 999L;
        when(authorService.getAuthorById(nonExistingAuthorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/api/authors/{id}", HttpMethod.DELETE, null, Void.class, nonExistingAuthorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(authorService, never()).deleteAuthor(nonExistingAuthorId);
    }

    @Test
    void testUpdateAuthorSuccess() throws URISyntaxException {
        // Arrange
        final String baseUrl = "http://localhost:" + port + "/api/authors";
        URI uri = new URI(baseUrl);

        // Create a new author for testing update
        Author existingAuthor = new Author();
        existingAuthor.setName("Original Author");

        ResponseEntity<Author> postResponse = this.restTemplate.postForEntity(uri, existingAuthor, Author.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        // Update the author
        URI updateUri = UriComponentsBuilder.fromUri(uri)
                .pathSegment(postResponse.getBody().getId().toString())
                .build().toUri();

        Author updatedAuthor = new Author();
        updatedAuthor.setId(postResponse.getBody().getId());
        updatedAuthor.setName("Updated Author");

        ResponseEntity<Author> updateResponse = this.restTemplate.exchange(
                updateUri, HttpMethod.PUT, new HttpEntity<>(updatedAuthor), Author.class);

        // Verify the response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Updated Author", Objects.requireNonNull(updateResponse.getBody()).getName());
    }

    @Test
    void testUpdateAuthorNotFound() {
        // Arrange
        Long nonExistingAuthorId = 999L;
        when(authorService.getAuthorById(nonExistingAuthorId)).thenReturn(Optional.empty());

        // Attempt to update the author
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/api/authors/{id}", HttpMethod.PUT, new HttpEntity<>(new Author()), Void.class, nonExistingAuthorId);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(authorService, never()).saveAuthor(org.mockito.ArgumentMatchers.any());
    }
}
