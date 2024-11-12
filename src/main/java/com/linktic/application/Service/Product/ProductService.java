package com.linktic.application.Service.Product;

import com.linktic.application.Exception.Product.ProductGeneralException;
import com.linktic.application.Exception.Product.ProductNotFoundException;
import com.linktic.core.Domain.Product.Product;
import com.linktic.core.Interface.Product.ProductServiceInterface;
import com.linktic.infrastructure.Repository.Product.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService implements ProductServiceInterface {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> createProduct(Product product) throws ProductGeneralException {
        try {
            logger.info("Creating product. {}", product.getName());
            return productRepository.save(product);
        }
        catch (Exception ex){
            throw new ProductGeneralException("An ocurred an error in create product " + ex);
        }
    }

    @Override
    public Mono<Product> getProductById(String id) {
        logger.info("Obtained product by ID product. " + id);
        return productRepository.findById(id).switchIfEmpty(Mono.error(() -> new ProductNotFoundException("Product not found with ID: " + id)));
    }

    @Override
    public Mono<Product> getProductByName(String name){
        logger.info("Get product by name." + name);
        return productRepository.findByName(name).switchIfEmpty(Mono.error( () -> new ProductNotFoundException("Product not found with name: " + name)));
    }

    @Override
    public Flux<Product> getAllProducts() {
        logger.info("Obtained all products. ");
        return productRepository.findAll().switchIfEmpty(Flux.error(() -> new ProductNotFoundException("Product not found with ID: ")));
    }

    @Override
    public Mono<Product> updateProduct(String id, Product product) throws ProductGeneralException {
        try{
            logger.info("Update produc by ID. ID: " + id + " Product: " + product.getName());
            return productRepository.findById(id)
                    .flatMap(existingProduct -> {
                        existingProduct.setName(product.getName());
                        existingProduct.setDescription(product.getDescription());
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setStock(product.getStock());
                        return productRepository.save(existingProduct);
                    });
        }catch (Exception ex){
            throw new ProductGeneralException("An ocurred an error in update product " + ex);
        }
    }

    @Override
    public Mono<Product> updateProductByName(String name, Product product) throws ProductGeneralException {
        try {
            logger.info("Update product by name " + name);
            return productRepository.findByName(name)
                    .switchIfEmpty(Mono.error( () -> new ProductNotFoundException("Product no found")))
                    .flatMap(existingProduct -> {
                        existingProduct.setName(product.getName());
                        existingProduct.setDescription(product.getDescription());
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setStock(product.getStock());
                        return productRepository.save(existingProduct);
                    });
        }catch (Exception ex){
            throw new ProductGeneralException("An ocurred an error in update product " + ex);
        }
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        logger.info("Delete product by id: " + id);
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new ProductNotFoundException("Product not found with ID: " + id)))
                .flatMap(product -> productRepository.deleteById(id));
    }
}
