package com.linktic.core.Interface.Order;

import com.linktic.core.Domain.Order.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderServiceInterface {
    Mono<Order> createOrder (Order order);
    Mono<Order> updateOrder (String id, Order order);
    Flux<Order> getAllOrders();
    Mono<Void> deleteOrder(String id);
    Mono<Order> getOrderById(String id);
    Mono<byte[]> generateExcelOrders();
}
