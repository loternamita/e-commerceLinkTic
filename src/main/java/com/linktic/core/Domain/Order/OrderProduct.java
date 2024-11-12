package com.linktic.core.Domain.Order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    private String nameProduct;
    private int quantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double price;
}
