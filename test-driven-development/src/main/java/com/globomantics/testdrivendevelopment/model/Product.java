package com.globomantics.testdrivendevelopment.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    private Integer version;

    public Product() {
    }

    public Product(Long id, String name, Integer quantity, Integer version) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.version = version;
    }

    public Product(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(quantity, product.quantity) && Objects.equals(version, product.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, version);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inventory=" + quantity +
                ", version=" + version +
                '}';
    }
}
