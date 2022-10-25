package com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.service;

import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.model.Product;
import com.globomantics.testdrivendevelopment.testdrivendevelopmentSQLbackend.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess() {
        //set up mock
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1L);

        //execute the service call
        Optional<Product> returnProduct = productService.findById(1L);

        //assertion the response
        assertTrue(returnProduct.isPresent(), "Product was not found");
        assertSame(returnProduct.get(), mockProduct, "Product should be the same");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        //set up mock
        doReturn(Optional.empty()).when(productRepository).findById(1L);

        //execute the service call
        Optional<Product> returnProduct = productService.findById(1L);

        //assertion the response
        assertFalse(returnProduct.isPresent(), "Product was found, when it shouldn't be");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        //set up mock
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        Product mockProduct2 = new Product(2L, "Product Name 2", 15, 3);
        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productRepository).findAll();

        //execute the service call
        List<Product> products = productService.findAll();

        //assertion the response
        assertEquals(2, products.size(), "findAll should return 2 products");
        assertSame(products.get(0), mockProduct, "Product 1 should be the same");
        assertSame(products.get(1), mockProduct2, "Product 2 should be the same");
    }

    @Test
    @DisplayName("Test save product")
    void testSave() {
        //set up mock
        Product mockProduct = new Product(1L, "Product Name", 10, 1);
        doReturn(mockProduct).when(productRepository).save(any());

        //execute the service call
        Product returnProduct = productService.save(mockProduct);

        //assertion the response
        assertNotNull(returnProduct, "The saved product should not be null");
        assertEquals(1, returnProduct.getVersion().intValue(),
                "The version for new product shoul be 1");
    }
}