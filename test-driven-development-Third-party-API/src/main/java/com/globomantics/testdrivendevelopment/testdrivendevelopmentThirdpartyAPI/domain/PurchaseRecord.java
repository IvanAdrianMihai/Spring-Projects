package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain;

import java.util.Objects;

public class PurchaseRecord {
    private Integer productId;
    private Integer quantityPurchased;

    public PurchaseRecord() {
    }

    public PurchaseRecord(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantityPurchased = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(Integer quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseRecord that = (PurchaseRecord) o;
        return Objects.equals(productId, that.productId) && Objects.equals(quantityPurchased, that.quantityPurchased);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantityPurchased);
    }

    @Override
    public String toString() {
        return "PurchaseRecord{" +
                "productId=" + productId +
                ", quantityPurchased=" + quantityPurchased +
                '}';
    }
}
