package com.globomantics.testdrivendevelopment.testdrivendevelopmentThirdpartyAPI.domain;

import java.io.Serializable;
import java.util.Objects;

public class InventoryRecord implements Serializable {
    private Integer productId;
    private Integer quantity;
    private String productName;
    private String productCategory;

    public InventoryRecord() {
    }

    public InventoryRecord(Integer productId, Integer quantity, String productName, String productCategory) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productCategory = productCategory;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryRecord that = (InventoryRecord) o;
        return Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity) && Objects.equals(productName, that.productName) && Objects.equals(productCategory, that.productCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, productName, productCategory);
    }

    @Override
    public String toString() {
        return "InventoryRecord{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }
}
