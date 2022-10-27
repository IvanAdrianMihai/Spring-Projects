package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.ReviewEntry;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository.MongoDataFile;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.repository.MongoSpringExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MongoSpringExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Test
    @DisplayName("GET /review/1 - Found")
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testGetReviewByIdFound() throws Exception {
        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/review/{id}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.version", Matchers.is(1)))
                .andExpect(jsonPath("$.entries.length()", Matchers.is(1)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("user1")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("This is a review")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.is("2018-11-10T11:38:26.855+00:00")));
    }

    @Test
    @DisplayName("GET /review/99 - Not Found")
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testGetReviewByIdNotFound() throws Exception {
        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/review/{id}", 99))
                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /review - Success")
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testCreateReview() throws Exception {
        //set up post Review
        Date now = new Date();
        Review postReview = new Review(1);
        postReview.getEntries().add(new ReviewEntry("test-user", now, "Great product"));

        //execute post request
        mockMvc.perform(MockMvcRequestBuilders.post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postReview)))

                //validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().exists(HttpHeaders.LOCATION))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.any(String.class)))
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.version", Matchers.is(1)))
                .andExpect(jsonPath("$.entries.length()", Matchers.is(1)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("test-user")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("Great product")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.any(String.class)));
    }

    @Test
    @DisplayName("POST /review/{productId}/entry")
    @MongoDataFile(value = "sample.json", classType = Review.class, collectionName = "Reviews")
    void testAddEntryToReview() throws Exception {
        //set up post Review
        Date now = new Date();
        ReviewEntry reviewEntry = new ReviewEntry("test-user", now, "Great product");

        mockMvc.perform(MockMvcRequestBuilders.post("/review/{productId}/entry", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reviewEntry)))

                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.version", Matchers.is(2)))
                .andExpect(jsonPath("$.entries.length()", Matchers.is(2)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("user1")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("This is a review")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.is("2018-11-10T11:38:26.855+00:00")))
                .andExpect(jsonPath("$.entries[1].username", Matchers.is("test-user")))
                .andExpect(jsonPath("$.entries[1].review", Matchers.is("Great product")))
                .andExpect(jsonPath("$.entries[1].date", Matchers.any(String.class)));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}