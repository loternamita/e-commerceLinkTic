package com.linktic.core.Domain.Order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String customerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime orderDate;
    private List<OrderProduct> products;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double totalAmount;
}