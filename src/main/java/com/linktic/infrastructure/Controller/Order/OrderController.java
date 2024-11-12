package com.linktic.infrastructure.Controller.Order;

import com.linktic.core.Domain.Order.Order;
import com.linktic.core.Interface.Order.OrderServiceInterface;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceInterface orderService;

    public OrderController(OrderServiceInterface orderServiceInterface) {
        this.orderService = orderServiceInterface;
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrderById(@PathVariable  String id) {
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrder(String id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/{id}")
    public Mono<Order> updateOrder(@PathVariable String id, @RequestBody Order order) {
        return orderService.updateOrder(id,order);
    }

    @GetMapping("/generateExcel")
    public Mono<ResponseEntity<byte[]>> generateExcel(){
        return orderService.generateExcelOrders()
                .map(excelData -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                    headers.setContentDispositionFormData("attachment", "Orders.xlsx");
                    headers.setContentLength(excelData.length);

                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(excelData);
                });
    }
}
