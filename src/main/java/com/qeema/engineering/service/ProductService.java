package com.qeema.engineering.service;

import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProductByID(Long id);

    void updateProducts(List<Product> products);
}
