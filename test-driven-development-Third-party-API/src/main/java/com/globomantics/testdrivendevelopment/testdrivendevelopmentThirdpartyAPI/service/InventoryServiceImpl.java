package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.InventoryRecord;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain.PurchaseRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Value("${inventorymanager.baseUrl}")
    private String baseUrl;

    //create a RestTemplate to use to communicate with the Inventory Manager Service
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<InventoryRecord> getInventoryRecord(Integer productId) {
        try {
            //get the inventory record for the specified product ID
            return Optional.of(restTemplate.getForObject(baseUrl + "/" + productId, InventoryRecord.class));
        } catch (HttpClientErrorException e) {
            //an exception occurred, so return Optional.empty()
            return Optional.empty();
        }
    }

    @Override
    public Optional<InventoryRecord> purchaseProduct(Integer productId, Integer quantityPurchased) {
        try {
            return Optional.of(restTemplate.postForObject(baseUrl + "/" + productId + "/purchaseRecord",
                    new PurchaseRecord(productId, quantityPurchased),
                    InventoryRecord.class));
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
