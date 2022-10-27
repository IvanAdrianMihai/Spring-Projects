package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> findById(String id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Optional<Review> findByProductId(Integer id) {
        return reviewRepository.findByProductId(id);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review save(Review review) {
        review.setVersion(1);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        review.setVersion(review.getVersion() + 1);
        return reviewRepository.save(review);
    }

    @Override
    public void delete(String id) {
        reviewRepository.deleteById(id);
    }
}
