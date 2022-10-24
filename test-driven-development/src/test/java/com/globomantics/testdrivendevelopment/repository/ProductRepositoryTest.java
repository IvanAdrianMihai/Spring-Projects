package com.globomantics.testdrivendevelopment.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.globomantics.testdrivendevelopment.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository productRepository;

    public ConnectionHolder getConnectionHolder() {
        //return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("products.yml")
    void testFindAll() {
        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size(), "We should have 2 products in our database");
    }

    @Test
    @DataSet("products.yml")
    void testFindByIdSuccess() {
        //find the product with id 2
        Optional<Product> product = productRepository.findById(2L);

        //validate that we found it
        assertTrue(product.isPresent(), "Product with ID 2 should be found");

        //validate the product values
        Product p = product.get();
        assertEquals(2, p.getId(), "Product ID should be 2");
        assertEquals("Product 2", p.getName(), "Product name should be \"Product 2\"");
        assertEquals(5, p.getQuantity(), "Product quantity should be 5");
        assertEquals(2, p.getVersion(), "Product version should be 2");
    }

    @Test
    @DataSet("products.yml")
    void testFindByNotFound() {
        //find the product with id 3
        Optional<Product> product = productRepository.findById(3L);

        //validate that we found it
        assertFalse(product.isPresent(), "Product with ID 3 should not be found");
    }

    @Test
    @DataSet("products.yml")
    void testSave() {
        //create a new product and save it to the database
        Product product = new Product("Product 5", 5);
        product.setVersion(1);
        Product savedProduct = productRepository.save(product);

        //validate the saved product
        assertEquals("Product 5", savedProduct.getName());
        assertEquals(5, savedProduct.getQuantity().intValue());

        //validate that we can get it back out of the databaze
        Optional<Product> loadedProduct = productRepository.findById(savedProduct.getId());
        assertTrue(loadedProduct.isPresent(), "Could not reload product from the database");
        assertEquals("Product 5", loadedProduct.get().getName(), "Product name should be \"Product 5\"");
        assertEquals(5, loadedProduct.get().getQuantity(), "Product quantity should be 5");
        assertEquals(1, loadedProduct.get().getVersion(), "Product version should be 1");
    }

    @Test
    @DataSet("products.yml")
    void testUpdateSuccess() {
        //update product 1's name, quantity and version
        Product product = new Product(1L, "This is product 1", 100, 5);
        boolean result = productRepository.update(product);

        //validate that our product is returned by update()
        assertTrue(result, "The product should have been updated");

        //retrieve product 1 from the database and validate its fields
        Optional<Product> loadedProduct = productRepository.findById(1L);
        assertTrue(loadedProduct.isPresent(), "Updated product should exist in the database");
        assertEquals("This is product 1", loadedProduct.get().getName(), "Product name should be \"This is product 1\"");
        assertEquals(100, loadedProduct.get().getQuantity(), "Product quantity should be 100");
        assertEquals(5, loadedProduct.get().getVersion(), "Product version should be 5");
    }

    @Test
    @DataSet("products.yml")
    void testUpdateFailure() {
        //try to update product 3's name, quantity and version
        Product product = new Product(3L, "This is product 3", 100, 5);
        boolean result = productRepository.update(product);

        //validate that our product is returned by update()
        assertFalse(result, "The product should not have been updated");
    }

    @Test
    @DataSet("products.yml")
    void testDeleteSuccess() {
        boolean result = productRepository.delete(1L);
        assertTrue(result, "Delete should return true on success");

        //validate that the product has been deleted
        Optional<Product> product = productRepository.findById(1L);
        assertFalse(product.isPresent(), "Product with ID 1 should have been deleted");
    }

    @Test
    @DataSet("products.yml")
    void tesDeleteFailure() {
        boolean result = productRepository.delete(3L);
        assertFalse(result, "Delete should return false because the deletion failed");
    }
}