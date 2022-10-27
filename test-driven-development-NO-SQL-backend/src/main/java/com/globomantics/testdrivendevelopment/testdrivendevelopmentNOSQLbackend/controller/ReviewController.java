package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.controller;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.ReviewEntry;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@RestController
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReview(@PathVariable String id) {
        return reviewService.findById(id)
                .map(review -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(review.getVersion()))
                                .location(new URI("/review/" + review.getId()))
                                .body(review);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reviews")
    public Iterable<Review> getReviews(@RequestParam(value = "productId", required = false) Optional<String> productId) {
        return productId.map(pid -> reviewService.findByProductId(Integer.valueOf(pid))
                .map(Arrays::asList)
                .orElseGet(ArrayList::new)).orElse(reviewService.findAll());
    }

    @PostMapping("/review")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        logger.info("Create new review for product id: {}, {}",
                review.getProductId(), review);

        //set the date for any entries in the review to now since we are creating the review now
        review.getEntries().forEach(entry -> entry.setDate(new Date()));

        //create new review
        Review newReview = reviewService.save(review);

        try {
            //build a created response
            return ResponseEntity
                    .created(new URI("/review/" + newReview.getId()))
                    .eTag(Integer.toString(newReview.getVersion()))
                    .body(newReview);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/review/{productId}/entry")
    public ResponseEntity<Review> addEntryToReview(@PathVariable Integer productId, @RequestBody ReviewEntry entry) {
        logger.info("Add review entry for product id: {}, {}", productId, entry);

        //retrieve the review for the specified productId; if there is no review, create a new one
        Review review = reviewService.findByProductId(productId).orElseGet(() -> new Review(productId));

        //add this new entry to the review
        entry.setDate(new Date());
        review.getEntries().add(entry);

        //save the review
        Review updateReview = reviewService.update(review);
        logger.info("Updated reviewL {}", updateReview);

        try {
            //build a created response
            return ResponseEntity
                    .ok()
                    .location(new URI("/review/" + updateReview.getId()))
                    .eTag(Integer.toString(updateReview.getVersion()))
                    .body(updateReview);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        logger.info("Deleting review with ID {}", id);

        //get the existing review
        Optional<Review> existingReview = reviewService.findById(id);

        //delete the review if it exists in the database
        return existingReview.map(review -> {
            reviewService.delete(review.getId());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
