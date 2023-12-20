package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Author;
import com.example.onlinebookstore.repository.AuthorRepository;
import com.example.onlinebookstore.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    public AuthorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(new Author(), new Author()));

        List<Author> authors = authorService.getAllAuthors();

        assertEquals(2, authors.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void testGetAuthorById() {
        Author mockAuthor = new Author();
        mockAuthor.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(mockAuthor));

        Optional<Author> author = authorService.getAuthorById(1L);

        assertTrue(author.isPresent());
        assertEquals(1L, author.get().getId());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveAuthor() {
        Author authorToSave = new Author();
        authorToSave.setName("John Doe");
        when(authorRepository.save(authorToSave)).thenReturn(authorToSave);

        Author savedAuthor = authorService.saveAuthor(authorToSave);

        assertNotNull(savedAuthor);
        assertEquals("John Doe", savedAuthor.getName());
        verify(authorRepository, times(1)).save(authorToSave);
    }

    @Test
    void testDeleteAuthor() {
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }
}
