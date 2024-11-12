package com.linktic.core.Interface.Product;

import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.core.Domain.Product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServiceInterface {
    Mono<Product> createProduct(Product product) throws ProductGeneralException;
    Mono<Product> getProductById(String id);
    Flux<Product> getAllProducts();
    Mono<Product> updateProduct(String id, Product product) throws ProductGeneralException;
    Mono<Void> deleteProduct(String id);
    Mono<Product> getProductByName(String name);
    Mono<Product> updateProductByName(String name, Product product) throws ProductGeneralException;
}
