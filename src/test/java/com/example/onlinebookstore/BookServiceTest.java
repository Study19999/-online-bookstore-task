package com.example.onlinebookstore;

import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(new Book(), new Book()));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        Book mockBook = new Book();
        mockBook.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        Optional<Book> book = bookService.getBookById(1L);

        assertTrue(book.isPresent());
        assertEquals(1L, book.get().getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveBook() {
        Book bookToSave = new Book();
        bookToSave.setTitle("Test Book");
        when(bookRepository.save(bookToSave)).thenReturn(bookToSave);

        Book savedBook = bookService.saveBook(bookToSave);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
    @Test
    void testSearchBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Hobbit");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Fellowship of the Ring");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByTitleContainingOrAuthor_NameContainingOrGenre_NameContaining(
                "The", null, null)).thenReturn(books);

        // Act
        List<Book> result = bookService.searchBooks("The", null, null);

        // Assert
        assertEquals(2, result.size());
        assertEquals("The Hobbit", result.get(0).getTitle());
        assertEquals("The Fellowship of the Ring", result.get(1).getTitle());

        verify(bookRepository, times(1)).findByTitleContainingOrAuthor_NameContainingOrGenre_NameContaining(
                "The", null, null);
    }
}
