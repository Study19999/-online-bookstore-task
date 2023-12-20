package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorTest {
    @Test
    public void testAuthorEntity() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        assertEquals(1L, author.getId());
        assertEquals("John Doe", author.getName());
    }
    @Test
    public void testAllArgsConstructor() {
        Author author = new Author(1L, "John Doe");

        assertEquals(1L, author.getId());
        assertEquals("John Doe", author.getName());
    }

}
