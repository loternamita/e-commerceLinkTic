package com.linktic.infrastructure.Controller.Product;


import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.core.Domain.Product.Product;
import com.linktic.core.Interface.Product.ProductServiceInterface;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceInterface productService;

    public ProductController(ProductServiceInterface productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) throws ProductGeneralException {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) throws ProductGeneralException {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/GetByName/{name}")
    public Mono<Product> getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @PutMapping("/UpdateByName/{name}")
    public Mono<Product> updateProductByName(@PathVariable String name, @RequestBody Product product) throws ProductGeneralException {
        return productService.updateProductByName(name, product);
    }
}

