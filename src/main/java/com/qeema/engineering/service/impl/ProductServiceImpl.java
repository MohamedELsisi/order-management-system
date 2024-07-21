package com.qeema.engineering.service.impl;

import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.ProductRepository;
import com.qeema.engineering.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class.getName());

    ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> getProductByID(Long productId) {
        logger.debug("Fetching product with ID: {}", productId);
        Optional<Product> existProduct = productRepository.findById(productId);
        if (existProduct.isPresent())
            logger.info("Product found with ID: {}", productId);
        else
            logger.warn("Product not found with ID: {}", productId);

        return existProduct;
    }

    @Override
    public void updateProducts(List<Product> products) {

        if (CollectionUtils.isEmpty(products)) {
            logger.warn("No products to update");
            throw new ResourceException("No products to update");
        }
        try {
            productRepository.saveAll(products);
            logger.info("Successfully updated {} products", products.size());
        } catch (Exception ex) {
            logger.error("Failed to update products. Error: {}", ex.getMessage(), ex);
            throw new ResourceException("Failed to update products");
        }
    }

}
