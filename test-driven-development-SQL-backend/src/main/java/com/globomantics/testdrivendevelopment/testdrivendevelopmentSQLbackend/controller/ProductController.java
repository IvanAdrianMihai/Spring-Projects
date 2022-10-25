package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.controller;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(product.getVersion()))
                                .location(new URI("/product/" + product.getId()))
                                .body(product);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products")
    public Iterable<Product> getProducts() {
        return productService.findAll();
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Create new product with name: {}, quantity: {}",
                product.getName(), product.getQuantity());

        //create new product
        Product newProduct = productService.save(product);

        try {
            //build a created response
            return ResponseEntity
                    .created(new URI("/product/" + newProduct.getId()))
                    .eTag(Integer.toString(newProduct.getVersion()))
                    .body(newProduct);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product,
                                           @PathVariable Long id,
                                           @RequestHeader("If-Match") Integer ifMatch) {
        logger.info("Updating product with name: {}, quantity: {}",
                product.getName(), product.getQuantity());

        //Get the existing product
        Optional<Product> existingProduct = productService.findById(id);

        return existingProduct.map(p -> {
            //compare the etags
            logger.info("Product with ID: {} has a version of {}. Update is for If-Match: {}",
                    product.getId(), product.getVersion(), ifMatch);
            if (!p.getVersion().equals(ifMatch)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            //update the product
            p.setName(product.getName());
            p.setQuantity(product.getQuantity());
            p.setVersion(p.getVersion() + 1);

            logger.info("Updating product with ID: {} -> name={}, quantity={}, version={}",
                    product.getId(), product.getName(), product.getQuantity(), product.getVersion());
            try {
                //update the product and return an ok response
                if (productService.update(p)) {
                    return ResponseEntity
                            .ok()
                            .location(new URI("/product/" + p.getId()))
                            .eTag(Integer.toString(p.getVersion()))
                            .body(p);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                //an error occurred trying to create the local URI, return an error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        logger.info("Deleting product with ID {}", id);

        //get the existing product
        Optional<Product> existingProduct = productService.findById(id);

        return existingProduct.map(p -> {
            if (productService.delete(p.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}