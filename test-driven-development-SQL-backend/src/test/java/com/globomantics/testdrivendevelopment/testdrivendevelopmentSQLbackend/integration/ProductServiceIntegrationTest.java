package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProductServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    public ConnectionHolder getConnectionHolder() {
        //return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("GET /product/1 - Found")
    @DataSet("products.yml")
    void testGetProductByIdFound() throws Exception {
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
                .andExpect(jsonPath("$.name", Matchers.is("Product 1")))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.version", Matchers.is(1)));
    }

    @Test
    @DisplayName("GET /product/3 - Not Found")
    @DataSet("products.yml")
    void testGetProductByIdNotFound() throws Exception {
        //execute the get request
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 3))
                //validate that we get a 404 Not Found response
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /product - Success")
    @DataSet("products-empty.yml")
    void testCreateProduct() throws Exception {
        //set up created Product service
        Product postProduct = new Product("Product Name", 10);

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
    @DisplayName("PUT /product/2 - Success")
    @DataSet("products.yml")
    void testUpdateProductSuccess() throws Exception {
        //set up created Product service
        Product putProduct = new Product("Product 2 Update", 10);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 2)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"3\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/2"))

                //validate the returned fields
                .andExpect(jsonPath("$.id", Matchers.is(2)))
                .andExpect(jsonPath("$.name", Matchers.is("Product 2 Update")))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.version", Matchers.is(3)));
    }

    @Test
    @DisplayName("PUT /product/1 - Version Mismatch")
    @DataSet("products.yml")
    void testUpdateProductVersionMismatch() throws Exception {
        //set up updateProduct service
        Product putProduct = new Product("Product Name", 10);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 7)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /product/3 - Not Found")
    @DataSet("products.yml")
    void testUpdateProductNotFound() throws Exception {
        //set up updateProduct service
        Product putProduct = new Product("Product Name", 10);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /product/1 - Success")
    @DataSet("products.yml")
    void testDeleteProductSuccess() throws Exception {
        //execute delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /product/3 - Not Found")
    @DataSet("products.yml")
    void testDeleteProductNotFound() throws Exception {
        //execute delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 3))
                .andExpect(status().isNotFound());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}