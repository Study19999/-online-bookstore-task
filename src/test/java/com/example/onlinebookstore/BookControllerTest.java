package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Author;
import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateBook() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/books";
        URI uri = new URI(baseUrl);

        // Create a new book
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setPrice(29.99);
        // Set other properties as needed

        // Perform POST request
        ResponseEntity<Book> responseEntity = this.restTemplate.postForEntity(uri, newBook, Book.class);

        // Add assertions based on your API response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("New Book", responseEntity.getBody().getTitle());
    }

    @Test
    void testDeleteBook() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + port + "/api/books";
        URI uri = new URI(baseUrl);

        // Create a new book for testing deletion
        Book newBook = new Book();
        newBook.setTitle("Book to Delete");
        newBook.setPrice(19.99);
        // Set other properties as needed

        ResponseEntity<Book> postResponse = this.restTemplate.postForEntity(uri, newBook, Book.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());

        // Delete the book
        URI deleteUri = UriComponentsBuilder.fromUri(uri)
                .pathSegment(postResponse.getBody().getId().toString())
                .build().toUri();

        ResponseEntity<Void> deleteResponse = this.restTemplate.exchange(deleteUri, HttpMethod.DELETE, null, Void.class);

        // Verify the response
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Verify the book is no longer present
        ResponseEntity<Book> getResponse = this.restTemplate.getForEntity(deleteUri, Book.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
