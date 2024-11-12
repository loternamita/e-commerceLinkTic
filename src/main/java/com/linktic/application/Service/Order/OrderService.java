package com.linktic.application.Service.Order;

import com.linktic.application.Exception.Order.OrderGeneralException;
import com.linktic.application.Exception.Order.OrderNotFoundException;
import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.application.Exception.Product.ProductNotFoundException;
import com.linktic.application.Shared.OrderExcelGenerator;
import com.linktic.core.Domain.Order.Order;
import com.linktic.core.Domain.Order.OrderProduct;
import com.linktic.core.Interface.Order.OrderServiceInterface;
import com.linktic.core.Interface.Product.ProductServiceInterface;
import com.linktic.infrastructure.Repository.Order.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class OrderService implements OrderServiceInterface {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductServiceInterface productClient;

    @Autowired
    private OrderExcelGenerator orderExcelGenerator;

    @Override
    public Mono<Order> createOrder(Order order) {
        logger.info("Create order");
        order.setOrderDate(LocalDateTime.now());
        return Flux.fromIterable(order.getProducts())
                .flatMap(orderProduct -> productClient.getProductByName(orderProduct.getNameProduct())
                        .switchIfEmpty(Mono.error(new ProductNotFoundException("Product " + orderProduct.getNameProduct() + " not found")))
                        .flatMap(product -> {

                            if (product.getStock() < orderProduct.getQuantity()) {
                                return Mono.error(new OrderGeneralException("Stock insufficient for product: " + product.getName()));
                            }

                            int stockUpdated = product.getStock() - orderProduct.getQuantity();
                            product.setStock(stockUpdated);
                            orderProduct.setPrice(product.getPrice());

                            try {
                                return productClient.updateProductByName(product.getName(), product).thenReturn(orderProduct);
                            } catch (ProductGeneralException e) {
                                return Mono.error(new ProductGeneralException("An occurred an error in update product by name: " + e));
                            }
                        })
                )
                .collectList()
                .flatMap(validatedProducts -> {

                    double totalAmount = validatedProducts.stream()
                            .mapToDouble(p -> p.getPrice() * p.getQuantity())
                            .sum();
                    order.setTotalAmount(totalAmount);
                    return orderRepository.save(order);
                });
    }

    @Override
    public Mono<Order> updateOrder(String id, Order order) {
        logger.info("Update order with ID: " + id);

        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order with ID: " + id + " not found")))
                .flatMap(existingOrder -> {
                    // Procesar productos de la orden actualizada
                    return Flux.fromIterable(order.getProducts())
                            .flatMap(orderProduct -> productClient.getProductByName(orderProduct.getNameProduct())
                                    .switchIfEmpty(Mono.error(new ProductNotFoundException("Product " + orderProduct.getNameProduct() + " not found")))
                                    .flatMap(product -> {
                                        int stockDelta = orderProduct.getQuantity() - getExistingOrderProductQuantity(existingOrder, orderProduct.getNameProduct());

                                        if (product.getStock() < stockDelta) {
                                            return Mono.error(new OrderGeneralException("Stock insufficient for product: " + product.getName()));
                                        }

                                        product.setStock(product.getStock() - stockDelta);
                                        orderProduct.setPrice(product.getPrice());

                                        try {
                                            return productClient.updateProductByName(product.getName(), product).thenReturn(orderProduct);
                                        } catch (ProductGeneralException e) {
                                            return Mono.error(new ProductGeneralException("An occurred an error in update product by name: " + e));
                                        }
                                    })
                            )
                            .collectList()
                            .flatMap(updatedProducts -> {
                                double totalAmount = updatedProducts.stream()
                                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                                        .sum();
                                order.setTotalAmount(totalAmount);
                                order.setOrderDate(existingOrder.getOrderDate()); // mantener la fecha de creación original
                                order.setId(id); // mantener el ID original
                                return orderRepository.save(order);
                            });
                });
    }

    private int getExistingOrderProductQuantity(Order existingOrder, String productName) {
        return existingOrder.getProducts().stream()
                .filter(p -> p.getNameProduct().equals(productName))
                .findFirst()
                .map(OrderProduct::getQuantity)
                .orElse(0);
    }

    @Override
    public Flux<Order> getAllOrders() {
        logger.info("Obtained all Orders");
        return orderRepository.findAll().switchIfEmpty(Flux.error(() -> new OrderNotFoundException("No orders found")));
    }

    @Override
    public Mono<Order> getOrderById(String id) {
        logger.info("Obtained order by id " + id);
        return orderRepository.findById(id).switchIfEmpty(Mono.error(new OrderNotFoundException("Not found order by id: " + id)));
    }

    @Override
    public Mono<Void> deleteOrder(String id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Not found order by id " + id + "for delete")))
                .flatMap(order -> orderRepository.deleteById(id));
    }

    public Mono<byte[]> generateExcelOrders(){
        return orderRepository.findAll().collectList()
                .flatMap(orders -> {
                    try {
                        byte[] excelData = orderExcelGenerator.generateOrderExcel(orders);
                        return Mono.just(excelData);
                    } catch (Exception e) {
                        return Mono.error(new OrderGeneralException("Occurred an error generating excel: " + e.getMessage()));
                    }
                });
    }
}
