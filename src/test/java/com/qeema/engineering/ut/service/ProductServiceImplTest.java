package com.qeema.engineering.ut.service;


import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.ProductRepository;
import com.qeema.engineering.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100);
        product.setQuantity(10);
    }

    @Test
    void getProductByID_ProductExists_ReturnsProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductByID(1L);
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void getProductByID_ProductDoesNotExist_ReturnsEmptyOptional() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductByID(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void updateProducts_EmptyProductList_ThrowsException() {
        List<Product> products = Collections.emptyList();
        assertThrows(ResourceException.class, () -> productService.updateProducts(products));
        verify(productRepository, never()).saveAll(products);
    }

    @Test
    void updateProducts_SaveAllThrowsException_ThrowsResourceException() {
        List<Product> products = List.of(product);
        doThrow(new RuntimeException("Database error")).when(productRepository).saveAll(any());
        assertThrows(ResourceException.class, () -> productService.updateProducts(products));
    }
}
