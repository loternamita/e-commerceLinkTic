package com.linktic.application.Exception.Order;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException( String message ){
        super(message);
    }
}
