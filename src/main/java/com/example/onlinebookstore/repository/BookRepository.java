package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthor_NameContainingOrGenre_NameContaining(
            String title, String authorName, String genreName);
}
