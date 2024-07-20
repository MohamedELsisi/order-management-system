package com.qeema.engineering.service.impl;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.mapper.OrderMapper;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.repository.ProductRepository;
import com.qeema.engineering.service.OrderService;
import com.qeema.engineering.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductService productService;
    OrderMapper orderMapper;


    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderMapper = orderMapper;
    }

    @Async
    @Override
    public CompletableFuture<Void> addOrder(OrderDTO orderDTO) {
        List<Product> productsToUpdate = new ArrayList<>();

        return CompletableFuture.runAsync(() -> {
            
            orderDTO.getProductList().forEach(productDTO -> {
                Product existingProduct = productService.getProductByID(productDTO.getId())
                        .orElseThrow(() -> new ResourceException("Product Not Found"));

                if (productDTO.getQuantity() >= existingProduct.getQuantity()) {
                    throw new ResourceException("Product quantity is less than the requested quantity");
                }

                existingProduct.setQuantity(existingProduct.getQuantity() - productDTO.getQuantity());
                productsToUpdate.add(existingProduct);
            });

            Order order = orderMapper.mapFromOrderDtoToOrderEntity(orderDTO);
            handelCreatTheOrder(order);
            updateTheProducts(productsToUpdate);

        });
    }

    @Override
    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::mapFromOrderEntityToOrderDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    protected void handelCreatTheOrder(Order order) {
        orderRepository.save(order);
    }

    private void updateTheProducts(List<Product> products) {
        productService.updateProducts(products);
    }

}
