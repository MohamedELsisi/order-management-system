package com.qeema.engineering.ut.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.exception.ValidationException;
import com.qeema.engineering.mapper.OrderMapper;
import com.qeema.engineering.mapper.OrderProductMapper;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.service.ProductService;
import com.qeema.engineering.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductService productService;
    @Mock
    OrderMapper orderMapper;
    @Mock
    OrderProductMapper orderProductMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDTO requestOrder;
    private List<ProductDTO> requestProducts;
    private OrderDTO validOrder;
    private Product product;

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        InputStream inputStream =
                new FileInputStream("src/test/resources/ut/newOrderRequest.json");
        requestOrder = objectMapper.readValue(inputStream,
                new TypeReference<>() {
                });

        inputStream =
                new FileInputStream("src/test/resources/ut/validOrder.json");
        validOrder = objectMapper.readValue(inputStream,
                new TypeReference<>() {
                });

        requestProducts = requestOrder.getProductList();
        product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(1L);
        product.setQuantity(20);
    }

    @Test
    void testCreateNewOrder() throws Exception {


        when(productService.getProductByID(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        CompletableFuture<Void> future = orderService.createNewOrder(validOrder);
        future.get();

        verify(productService, times(1)).getProductByID(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productService, times(1)).updateProducts(anyList());
    }

    @Test
    @DisplayName("test create new order contain NotFOUND PRODUCT")
    void testCreateNewOrder_ProductNotFound() {
        when(productService.getProductByID(1L)).thenReturn(Optional.empty());

        CompletableFuture<Void> future = orderService.createNewOrder(validOrder);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof ResourceException);
        assertEquals("Product Not Found with ID: 1", exception.getCause().getMessage());

        verify(productService, times(1)).getProductByID(1L);
        verify(orderRepository, never()).save(any(Order.class));
        verify(productService, never()).updateProducts(anyList());
    }

    @Test
    @DisplayName("test create new order contain Not ENOUGH PRODUCT QUANTITY")
    void testCreateNewOrder_NotEnoughProductQuantity() {
        validOrder.getProductList().get(0).setQuantity(30);


        when(productService.getProductByID(1L)).thenReturn(Optional.of(product));

        CompletableFuture<Void> future = orderService.createNewOrder(validOrder);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof ResourceException);

        assertEquals("Product quantity is less than the requested quantity", exception.getCause().getMessage());

        verify(productService, times(1)).getProductByID(1L);
        verify(orderRepository, never()).save(any(Order.class));
        verify(productService, never()).updateProducts(anyList());
    }

    @Test
    @DisplayName("try to add Duplicate product ")
    void testCreateNewOrder_DuplicateProductIds() {

        requestProducts.get(1).setId(requestProducts.get(0).getId());
        assertThrows(ValidationException.class,
                () -> orderService.createNewOrder(requestOrder));
        verify(orderRepository, never()).save(any(Order.class));

    }

    @Test
    @DisplayName("create new empty order then throw exception")
    void testCreateNewEmptyOrderWithException() {
        assertThrows(ValidationException.class,
                () -> orderService.createNewOrder(new OrderDTO()));
    }

    @Test
    @DisplayName("try to add product with Invalid Id with  then throw exception")
    void testCreateOrderInvalidProductId() {
        requestProducts.get(0).setId(null);
        assertThrows(ValidationException.class,
                () -> orderService.createNewOrder(requestOrder));
        verify(productService, never()).getProductByID(any());
    }

    @Test
    @DisplayName("try to add invalid product price")
    void testCreateNewOrderInvalidProductPrice() {
        requestProducts.get(0).setPrice(0);

        assertThrows(ValidationException.class,
                () -> orderService.createNewOrder(requestOrder));
        verify(productService, never()).getProductByID(any());
    }

    @Test
    @DisplayName("try to add invalid product quantity")
    void testCreateNewOrderInvalidProductQuantity() {
        requestProducts.get(0).setQuantity(0);

        assertThrows(ValidationException.class,
                () -> orderService.createNewOrder(requestOrder));
        verify(productService, never()).getProductByID(any());
    }

}
