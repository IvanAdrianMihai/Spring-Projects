package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.service.ProductService;
import org.hamcrest.Matchers;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /product/1 - Found")
    void testGetProductByIdFound() throws Exception {
        //set up our mocked service
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);

        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Product Name")))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.version", Matchers.is(1)));
    }

    @Test
    @DisplayName("GET /product/1 - Not Found")
    void testGetProductByIdNotFound() throws Exception {
        //set up mocked service
        doReturn(Optional.empty()).when(productService).findById(1L);

        //execute the get request
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1))
                //validate that we get a 404 Not Found response
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /product - Success")
    void testCreateProduct() throws Exception {
        //set up mocked service
        Product postProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        doReturn(mockProduct).when(productService).save(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postProduct)))

                //validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Product Name")))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.version", Matchers.is(1)));
    }

    @Test
    @DisplayName("PUT /product/1 - Success")
    void testUpdateProductSuccess() throws Exception {
        //set up mocked service
        Product putProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).update(any());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Product Name")))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.version", Matchers.is(2)));
    }

    @Test
    @DisplayName("PUT /product/1 - Version Mismatch")
    void testUpdateProductVersionMismatch() throws Exception {
        //set up mocked service
        Product putProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1L, "Product Name", 10, 2);
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).update(any());

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /product/1 - Not Found")
    void testUpdateProductNotFound() throws Exception {
        //set up mocked service
        Product putProduct = new Product("Product Name", 10);
        doReturn(Optional.empty()).when(productService).findById(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /product/1 - Success")
    void testDeleteProductSuccess() throws Exception {
        //set up mock product
        Product mockProduct = new Product(1L, "Product Name", 10, 1);

        //set up mocked service
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(true).when(productService).delete(1L);

        //execute delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /product/1 - Not Found")
    void testDeleteProductNotFound() throws Exception {
        //set up mocked service
        doReturn(Optional.empty()).when(productService).findById(1L);

        //execute delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /product/1 - Failure")
    void testDeleteProductFailure() throws Exception {
        //set up mock product
        Product mockProduct = new Product(1L, "Product Name", 10, 1);

        //set up mocked service
        doReturn(Optional.of(mockProduct)).when(productService).findById(1L);
        doReturn(false).when(productService).delete(1L);

        //execute delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isInternalServerError());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}