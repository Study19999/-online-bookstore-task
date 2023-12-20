package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Author;
import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.entity.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookTest {
    @Test
    public void testBookEntity() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fiction");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPrice(19.99);
        book.setQuantityAvailable(10);

        assertEquals(1L, book.getId());
        assertEquals("Sample Book", book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(genre, book.getGenre());
        assertEquals(19.99, book.getPrice());
        assertEquals(10, book.getQuantityAvailable());
    }
    @Test
    public void testAllArgsConstructor() {
        Author author = new Author(1L, "John Doe");
        Genre genre = new Genre(1L, "Fiction");

        Book book = new Book(1L, "Sample Book", author, genre, 19.99, 10);

        assertEquals(1L, book.getId());
        assertEquals("Sample Book", book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(genre, book.getGenre());
        assertEquals(19.99, book.getPrice());
        assertEquals(10, book.getQuantityAvailable());
    }

}

