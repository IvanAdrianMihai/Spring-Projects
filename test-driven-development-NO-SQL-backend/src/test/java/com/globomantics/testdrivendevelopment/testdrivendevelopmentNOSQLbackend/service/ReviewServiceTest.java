package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.ReviewEntry;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReviewServiceTest {
    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        //setup our mock
        Review mockReview = new Review("reviewId", 1, 1);
        Date now = new Date();
        mockReview.getEntries().add(new ReviewEntry("test-user", now, "Great product"));
        doReturn(Optional.of(mockReview)).when(reviewRepository).findById("reviewId");

        //execute the service call
        Optional<Review> returnReview = reviewService.findById("reviewId");

        //assert the response
        assertTrue(returnReview.isPresent(), "Review was not found");
        assertSame(mockReview, returnReview.get(), "Review should be the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        //setup our mock
        doReturn(Optional.empty()).when(reviewRepository).findById("1");

        //execute the service call
        Optional<Review> returnReview = reviewService.findById("1");

        //assert the response
        assertFalse(returnReview.isPresent(), "Review was found, when it shouldn't be");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        //setup our mock
        Review mockReview = new Review("reviewId", 1, 1);
        Review mockReview2 = new Review("reviewId2", 2, 1);
        doReturn(Arrays.asList(mockReview, mockReview2)).when(reviewRepository).findAll();

        //execute the service call
        List<Review> reviews = reviewService.findAll();

        //assert the response
        assertEquals(2, reviews.size(), "findAll should return 2 reviews");
    }

    @Test
    @DisplayName("Rest save review")
    void testSave() {
        //setup our mock
        Review mockReview = new Review("reviewId", 1, 1);
        doReturn(mockReview).when(reviewRepository).save(any());

        //execute the service call
        Review returnReview = reviewService.save(mockReview);

        //assert the response
        assertNotNull(returnReview, "The saved review should not be null");
        assertEquals(1, returnReview.getVersion().intValue(), "The version for a new review should be 1");
    }
}