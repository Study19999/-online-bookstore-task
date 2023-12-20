package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Genre;
import com.example.onlinebookstore.repository.GenreRepository;
import com.example.onlinebookstore.service.GenreService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    public GenreServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGenres() {
        when(genreRepository.findAll()).thenReturn(Arrays.asList(new Genre(), new Genre()));

        List<Genre> genres = genreService.getAllGenres();

        assertEquals(2, genres.size());
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void testGetGenreById() {
        Genre mockGenre = new Genre();
        mockGenre.setId(1L);
        when(genreRepository.findById(1L)).thenReturn(Optional.of(mockGenre));

        Optional<Genre> genre = genreService.getGenreById(1L);

        assertTrue(genre.isPresent());
        assertEquals(1L, genre.get().getId());
        verify(genreRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveGenre() {
        Genre genreToSave = new Genre();
        genreToSave.setName("Fiction");
        when(genreRepository.save(genreToSave)).thenReturn(genreToSave);

        Genre savedGenre = genreService.saveGenre(genreToSave);

        assertNotNull(savedGenre);
        assertEquals("Fiction", savedGenre.getName());
        verify(genreRepository, times(1)).save(genreToSave);
    }

    @Test
    void testDeleteGenre() {
        doNothing().when(genreRepository).deleteById(1L);

        genreService.deleteGenre(1L);

        verify(genreRepository, times(1)).deleteById(1L);
    }
}
