package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.ReviewEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ReviewRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    //Jackson ObjectMapper - used to load a JSON file into a list of Reviews
    private ObjectMapper mapper = new ObjectMapper();

    //the path to our Sample JSON file
    private static File SAMPLE_JSON = Paths.get("src", "test", "resources", "data", "sample.json").toFile();

    @BeforeEach
    void beforeEach() throws Exception {
        //deserialize our JSON file to an array of reviews
        Review[] objects = mapper.readValue(SAMPLE_JSON, Review[].class);

        //load each review into MongoDB
        Arrays.stream(objects).forEach(mongoTemplate::save);
    }

    @AfterEach
    void afterEach() {
        //drop the reviews collection so we can start fresh
        mongoTemplate.dropCollection("Reviews");
    }

    @Test
    void testFindAll() {
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(2, reviews.size(), "Should be two reviews in the database");
    }

    @Test
    void findByIdSuccess() {
        Optional<Review> review = reviewRepository.findById("1");
        assertTrue(review.isPresent(), "We should have found a review with ID 1");
        review.ifPresent(r -> {
            assertEquals("1", r.getId(), "Review ID should be 1");
            assertEquals(1, r.getProductId().intValue(), "Review Product ID should be 1");
            assertEquals(1, r.getVersion().intValue(), "Review version should be 1");
            assertEquals(1, r.getEntries().size(), "Review 1 should have 1 entry");
        });
    }

    @Test
    void testFindByIdFailure() {
        Optional<Review> review = reviewRepository.findById("3");
        assertFalse(review.isPresent(), "We should not find a review with ID 3");
    }

    @Test
    void testFindByProductIdSuccess() {
        Optional<Review> review = reviewRepository.findByProductId(1);
        assertTrue(review.isPresent(), "There should be a review for product ID 1");
    }

    @Test
    void testFindByProductIdFailure() {
        Optional<Review> review = reviewRepository.findByProductId(3);
        assertFalse(review.isPresent(), "We should not be a review for product with ID 3");
    }

    @Test
    void testSave() {
        //create a test review
        Review review = new Review(10, 1);
        review.getEntries().add(new ReviewEntry("test-user", new Date(), "This is a review"));

        //persist the review to MongoDB
        Review savedReview = reviewRepository.save(review);

        //retrieve the review
        Optional<Review> loadedReview = reviewRepository.findById(savedReview.getId());

        //validations
        assertTrue(loadedReview.isPresent());
        loadedReview.ifPresent(r -> {
            assertEquals(10, r.getProductId().intValue());
            assertEquals(1, r.getVersion().intValue(), "Review version should be 1");
            assertEquals(1, r.getEntries().size(), "Review 1 should have one entry");
        });
    }

    @Test
    void testUpdate() {
        //retrieve review 2
        Optional<Review> review = reviewRepository.findById("2");
        assertTrue(review.isPresent(), "Review 2 should be present");
        assertEquals(3, review.get().getEntries().size(), "There should be 3 review entries");

        //add an entry to the review and save
        Review reviewToUpdate = review.get();
        reviewToUpdate.getEntries().add(new ReviewEntry("test-user2", new Date(), "This is a review"));
        reviewRepository.save(reviewToUpdate);

        //retrieve the review again and validate that it now has 4 entries
        Optional<Review> updatedReview = reviewRepository.findById("2");
        assertTrue(updatedReview.isPresent(), "Review 2 should be present");
        assertEquals(4, updatedReview.get().getEntries().size(), "There should be 4 review entries");
    }

    @Test
    void testDelete() {
        //delete review 2
        reviewRepository.deleteById("2");

        //confirm that it is no longer in the database
        Optional<Review> review = reviewRepository.findById("2");
        assertFalse(review.isPresent(), "Review 2 should now be deleted from the database");
    }
}