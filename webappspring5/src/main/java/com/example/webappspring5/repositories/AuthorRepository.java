package com.example.webappspring5.repositories;

import com.example.webappspring5.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
