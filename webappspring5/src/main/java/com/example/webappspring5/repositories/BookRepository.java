package com.example.webappspring5.repositories;

import com.example.webappspring5.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
