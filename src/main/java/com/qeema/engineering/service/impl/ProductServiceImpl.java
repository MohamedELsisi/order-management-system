package com.qeema.engineering.service.impl;

import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.ProductRepository;
import com.qeema.engineering.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Optional<Product> getProductByID(ProductDTO productDTO) {
        Optional<Product> existProduct = productRepository.findById(productDTO.getId());
        if (existProduct.isPresent())
            return existProduct;
        else
            return Optional.empty();
    }

    @Override
    @Transactional
    public void updateProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
}
