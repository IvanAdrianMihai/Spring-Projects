package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.InventoryRecord;

import java.util.Optional;

public interface InventoryService {
    Optional<InventoryRecord> getInventoryRecord(Integer productId);

    Optional<InventoryRecord> purchaseProduct(Integer productId, Integer quantityPurchased);
}
