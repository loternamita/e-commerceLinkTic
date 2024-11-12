package com.linktic.infrastructure.Repository.Order;

import com.linktic.core.Domain.Order.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}