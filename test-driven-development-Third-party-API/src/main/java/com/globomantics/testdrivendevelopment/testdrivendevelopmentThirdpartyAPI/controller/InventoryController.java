package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.controller;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.PurchaseRecord;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<?> getInventoryRecord(@PathVariable Integer id) {
        return inventoryService.getInventoryRecord(id)
                .map(inventoryRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/inventory/" + inventoryRecord.getProductId()))
                                .body(inventoryRecord);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory/purchase-record")
    public ResponseEntity<?> createProduct(@RequestBody PurchaseRecord purchaseRecord) {
        logger.info("Create new purchase record: {}", purchaseRecord);

        return inventoryService.purchaseProduct(purchaseRecord.getProductId(), purchaseRecord.getQuantityPurchased())
                .map(inventoryRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/inventory/" + inventoryRecord.getProductId()))
                                .body(inventoryRecord);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
