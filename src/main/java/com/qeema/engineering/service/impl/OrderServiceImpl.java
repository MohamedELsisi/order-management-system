package com.qeema.engineering.service.impl;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.mapper.OrderMapper;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.OrderProduct;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.service.OrderService;
import com.qeema.engineering.service.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class.getName());

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
    @Transactional
    public CompletableFuture<Void> addOrder(OrderDTO orderDTO) {
        List<Product> productsToUpdate = new ArrayList<>();
        Order order = new Order();


        logger.info("creating new order");
        return CompletableFuture.runAsync(() -> {

            orderDTO.getProductList().forEach(productDTO -> {
                logger.info("get product {} from database", productDTO.getId());

                Product existingProduct = productService.getProductByID(productDTO.getId()).orElseThrow(() -> {
                    logger.info("Product {} not found", productDTO.getId());
                    return new ResourceException("Product Not Found with ID: " + productDTO.getId());
                });

                if (productDTO.getQuantity() >= existingProduct.getQuantity()) {
                    logger.error("product {} is over quantity", productDTO.getId());
                    throw new ResourceException("Product quantity is less than the requested quantity");
                }

                existingProduct.setQuantity(existingProduct.getQuantity() - productDTO.getQuantity());

                productsToUpdate.add(existingProduct);

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(existingProduct);
                orderProduct.setQuantity(productDTO.getQuantity());
                orderProduct.setPrice(productDTO.getPrice());

                order.getOrderProducts().add(orderProduct);
            });
            handelCreatTheOrder(order, productsToUpdate);
        });
    }

    @Override
    public List<OrderDTO> getOrders() {
        logger.info("get all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setProductList(order.getOrderProducts().stream()
                            .map(orderProduct -> {
                                Product product = orderProduct.getProduct();
                                return new ProductDTO(product.getId(), product.getName(), product.getPrice(), orderProduct.getQuantity());
                            })
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }


    protected void handelCreatTheOrder(Order order,List<Product> products) {
        try {
            logger.info("save the new order");
            orderRepository.save(order);

            logger.info("update the products");
            productService.updateProducts(products);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }

}
