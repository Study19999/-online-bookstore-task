package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GenreTest {
    @Test
    public void testGenreEntity() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Fiction");

        assertEquals(1L, genre.getId());
        assertEquals("Fiction", genre.getName());
    }
    @Test
    public void testAllArgsConstructor() {
        Genre genre = new Genre(1L, "Fiction");

        assertEquals(1L, genre.getId());
        assertEquals("Fiction", genre.getName());
    }
}

