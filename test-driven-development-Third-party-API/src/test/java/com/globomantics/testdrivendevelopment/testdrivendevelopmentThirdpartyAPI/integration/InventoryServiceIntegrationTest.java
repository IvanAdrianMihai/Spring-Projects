package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.PurchaseRecord;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
class InventoryServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {
        //start de WireMock server
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();

        //auto configure from mappings directory
    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("GET /inventory/1 - Success")
    void testGetInventoryRecordSuccess() throws Exception {
        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/{id}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.quantity", Matchers.is(500)))
                .andExpect(jsonPath("$.productName", Matchers.is("Super Great Product")))
                .andExpect(jsonPath("$.productCategory", Matchers.is("Great Products")));
    }

    @Test
    @DisplayName("GET /inventory/2 - Not Found")
    void testGetInventoryRecordNotFound() throws Exception {
        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/{id}", 2))
                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /inventory/purchase-record - Success")
    void testCreatePurchaseRecord() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/inventory/purchase-record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new PurchaseRecord(1, 5))))

                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.quantity", Matchers.is(499)))
                .andExpect(jsonPath("$.productName", Matchers.is("Super Great Product")))
                .andExpect(jsonPath("$.productCategory", Matchers.is("Great Products")));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}