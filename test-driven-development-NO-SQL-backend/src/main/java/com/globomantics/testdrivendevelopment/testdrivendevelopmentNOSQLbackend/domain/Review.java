package com.globomantics.testdrivendevelopment.testdrivendevelopmentNOSQLbackend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "Reviews")
public class Review {
    @Id
    private String id;
    private Integer productId;
    private Integer version =1;
    private List<ReviewEntry> entries =new ArrayList<>();

    public Review() {
    }

    public Review(Integer productId) {
        this.productId = productId;
    }

    public Review(Integer productId, Integer version) {
        this.productId = productId;
        this.version = version;
    }

    public Review(String id, Integer productId, Integer version) {
        this.id = id;
        this.productId = productId;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ReviewEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ReviewEntry> entries) {
        this.entries = entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(productId, review.productId) && Objects.equals(version, review.version) && Objects.equals(entries, review.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, version, entries);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", version=" + version +
                ", entries=" + entries +
                '}';
    }
}
