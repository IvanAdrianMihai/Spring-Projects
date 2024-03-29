package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.Review;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain.ReviewEntry;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.service.ReviewService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {
    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    //create a date format that we can use to compare SpringMVC returned dates to expected values
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00");

    @BeforeAll
    static void beforeAll() {
        //spring's date are configured to GMT, so adjust our timezone accordingly
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    @DisplayName("GET /review/1 - Found")
    void testGetReviewByIdFound() throws Exception {
        //set up our mocked service
        Review mockReview = new Review("reviewId", 1, 1);
        Date now = new Date();
        mockReview.getEntries().add(new ReviewEntry("test-user", now, "Great product"));
        doReturn(Optional.of(mockReview)).when(reviewService).findById("reviewId");

        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/review/{id}", "reviewId"))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/reviewId"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is("reviewId")))
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.version", Matchers.is(1)))
                .andExpect(jsonPath("$.entries.length()", Matchers.is(1)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("test-user")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("Great product")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.is(df.format(now))));
    }

    @Test
    @DisplayName("GET /review/1 - Not Found")
    void testGetReviewByIdNotFound() throws Exception {
        //set up our mocked service
        doReturn(Optional.empty()).when(reviewService).findById("reviewId");

        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/review/{id}", "reviewId"))
                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /review - Success")
    void testCreateReview() throws Exception {
        //set up our mocked service
        Date now = new Date();
        Review postReview = new Review("reviewId", 1, 1);
        postReview.getEntries().add(new ReviewEntry("test-user", now, "Great product"));

        Review mockReview = new Review("reviewId", 1, 1);
        mockReview.getEntries().add(new ReviewEntry("test-user", now, "Great product"));

        doReturn(mockReview).when(reviewService).save(any());

        //execute post request
        mockMvc.perform(MockMvcRequestBuilders.post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockReview)))

                //validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/review/reviewId"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is("reviewId")))
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.version", Matchers.is(1)))
                .andExpect(jsonPath("$.entries.length()", Matchers.is(1)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("test-user")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("Great product")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.is(df.format(now))));
    }

    @Test
    @DisplayName("POST /review/{productId}/entry")
    void testAddEntryToReview() throws Exception {
        //set up our mocked service
        Date now = new Date();
        ReviewEntry reviewEntry = new ReviewEntry("test-user", now, "Great product");
        Review mockReview = new Review("1", 1, 1);
        Review returnedReview = new Review("1", 1, 2);
        returnedReview.getEntries().add(reviewEntry);

        //handle lookup
        doReturn(Optional.of(mockReview)).when(reviewService).findByProductId(1);

        //handle save
        doReturn(returnedReview).when(reviewService).update(any());

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
                .andExpect(jsonPath("$.entries.length()", Matchers.is(1)))
                .andExpect(jsonPath("$.entries[0].username", Matchers.is("test-user")))
                .andExpect(jsonPath("$.entries[0].review", Matchers.is("Great product")))
                .andExpect(jsonPath("$.entries[0].date", Matchers.is(df.format(now))));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}