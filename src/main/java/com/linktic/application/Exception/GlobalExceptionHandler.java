package com.linktic.application.Exception;

import com.linktic.application.Exception.Order.OrderGeneralException;
import com.linktic.application.Exception.Order.OrderNotFoundException;
import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.application.Exception.Product.ProductNotFoundException;
import com.linktic.core.Domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException (ProductNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse("404", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderGeneralException.class)
    public ResponseEntity<ErrorResponse> handleOrderGeneralException (OrderGeneralException ex){
        ErrorResponse errorResponse = new ErrorResponse("500", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderGeneralException (OrderNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse("404", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductGeneralException.class)
    public ResponseEntity<ErrorResponse> handleProductGeneralException (ProductGeneralException ex){
        ErrorResponse errorResponse = new ErrorResponse("500", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
