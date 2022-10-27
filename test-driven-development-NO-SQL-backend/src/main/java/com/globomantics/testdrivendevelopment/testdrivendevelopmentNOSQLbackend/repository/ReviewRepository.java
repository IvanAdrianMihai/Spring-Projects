package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findByProductId(Integer id);
}
