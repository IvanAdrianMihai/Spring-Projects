package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.InventoryRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {
        //start de WireMock server
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();

        //configure requests
        wireMockServer.stubFor(get(urlEqualTo("/inventory/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json/inventory-response.json")));
        wireMockServer.stubFor(get(urlEqualTo("/inventory/2"))
                .willReturn(aResponse().withStatus(404)));
        wireMockServer.stubFor(post(urlEqualTo("/inventory/1/purchaseRecord"))
                //actual header sent by the RestTemplate is: application/json;charset=UTF-8
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(containing("\"productId\":1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json/inventory-response-after-post.json")));
    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    void testGetInventoryRecordSuccess() {
        Optional<InventoryRecord> record = inventoryService.getInventoryRecord(1);
        assertTrue(record.isPresent(), "InventoryRecord should be present");

        //validate the contents of the response
        assertEquals(500, record.get().getQuantity().intValue(), "The quantity should be 500");
    }

    @Test
    void testGetInventoryRecordNotFound() {
        Optional<InventoryRecord> record = inventoryService.getInventoryRecord(2);
        assertFalse(record.isPresent(), "InventoryRecord should not be present");
    }

    @Test
    void testPurchaseProductSuccess() {
        Optional<InventoryRecord> record = inventoryService.purchaseProduct(1, 1);
        assertTrue(record.isPresent(), "InventoryRecord should be present");

        //validate the contents of the response
        assertEquals(499, record.get().getQuantity().intValue(), "The quantity should be 499");
    }
}