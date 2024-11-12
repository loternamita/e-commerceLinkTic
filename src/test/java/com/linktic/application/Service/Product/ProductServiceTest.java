package com.linktic.application.Service.Product;

import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.application.Exception.Product.ProductNotFoundException;
import com.linktic.core.Domain.Product.Product;
import com.linktic.infrastructure.Repository.Product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() throws ProductGeneralException {
        Product product = new Product("1A4JI","Pants", "Comfortable pants", 25000, 50);
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        Product createdProduct = productService.createProduct(product).block();

        assertNotNull(createdProduct);
        assertEquals("Pants", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById() {
        Product product = new Product("1A4JI","Shirt", "Casual shirt", 1500, 30);
        when(productRepository.findById("1")).thenReturn(Mono.just(product)); // Cambia a Mono.just

        Optional<Product> foundProduct = productService.getProductById("1").blockOptional();

        assertTrue(foundProduct.isPresent());
        assertEquals("Shirt", foundProduct.get().getName());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateProduct() throws ProductGeneralException {
        Product existingProduct = new Product("1A4JI","Shoes", "Running shoes", 3000, 20);
        existingProduct.setId("1");
        Product updatedProduct = new Product("1A4JI","Shoes", "Updated description", 3200, 15);

        when(productRepository.findById("1")).thenReturn(Mono.just(existingProduct)); // Cambia a Mono.just
        when(productRepository.save(existingProduct)).thenReturn(Mono.just(updatedProduct)); // Cambia a Mono.just

        Product result = productService.updateProduct("1", updatedProduct).block();

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());
        assertEquals(3200, result.getPrice());
        verify(productRepository, times(1)).findById("1");
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testDeleteProduct_ProductExists() {
        // Caso en el que el producto existe
        Product product = new Product("1A4JI", "Hat", "Stylish hat", 1000, 10);
        product.setId("1");

        when(productRepository.findById("1")).thenReturn(Mono.just(product));
        when(productRepository.deleteById("1")).thenReturn(Mono.empty());

        productService.deleteProduct("1").block();

        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Caso en el que el producto no existe
        when(productRepository.findById("1")).thenReturn(Mono.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct("1").block();
        });

        verify(productRepository, times(0)).deleteById("1");
    }
}
