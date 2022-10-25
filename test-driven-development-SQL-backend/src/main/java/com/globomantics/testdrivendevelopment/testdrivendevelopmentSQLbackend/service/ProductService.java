package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(Long id);

    List<Product> findAll();

    Product save(Product product);

    boolean update(Product product);

    boolean delete(Long id);
}
