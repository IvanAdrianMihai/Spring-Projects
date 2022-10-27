package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.InventoryRecord;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.PurchaseRecord;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.service.InventoryService;
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

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {
    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /inventory/1 - Success")
    void testGetInventoryRecordSuccess() throws Exception {
        //set up our mocked service
        InventoryRecord mockRecord = new InventoryRecord(1, 10,
                "Product 1", "Great Products");
        doReturn(Optional.of(mockRecord)).when(inventoryService).getInventoryRecord(1);

        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/{id}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //validate the headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))

                //validate the returned fields
                .andExpect(jsonPath("$.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.productName", Matchers.is("Product 1")))
                .andExpect(jsonPath("$.productCategory", Matchers.is("Great Products")));
    }

    @Test
    @DisplayName("GET /inventory/1 - Not Found")
    void testGetInventoryRecordNotFound() throws Exception {
        //set up our mocked service
        doReturn(Optional.empty()).when(inventoryService).getInventoryRecord(1);

        //execute get request
        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/{id}", 1))
                //validate the response code and content type
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /inventory/purchase-record - Success")
    void testCreatePurchaseRecord() throws Exception {
        //setup mocked service
        InventoryRecord mockRecord = new InventoryRecord(1, 10,
                "Product 1", "Great Products");
        doReturn(Optional.of(mockRecord)).when(inventoryService).purchaseProduct(1, 5);

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
                .andExpect(jsonPath("$.quantity", Matchers.is(10)))
                .andExpect(jsonPath("$.productName", Matchers.is("Product 1")))
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