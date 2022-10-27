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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class InventoryServiceMappingTest {

    @Autowired
    private InventoryService inventoryService;

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