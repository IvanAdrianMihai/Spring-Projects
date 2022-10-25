package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            products.add(product);
        }
        return products;
    }

    @Override
    public Product save(Product product) {
        product.setVersion(1);
        return productRepository.save(product);
    }

    @Override
    public boolean update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public boolean delete(Long id) {
        return productRepository.delete(id);
    }
}
