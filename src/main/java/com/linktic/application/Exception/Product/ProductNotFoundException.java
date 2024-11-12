package com.linktic.application.Exception.Product;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException( String message ){
        super(message);
    }
}
