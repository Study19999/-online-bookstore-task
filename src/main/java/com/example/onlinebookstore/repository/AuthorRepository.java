package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}