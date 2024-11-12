package com.linktic.infrastructure.Repository.Product;

import com.linktic.core.Domain.Product.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Mono<Product> findByName(String name);
}
