package com.qeema.engineering.service.impl;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.mapper.OrderMapper;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.repository.ProductRepository;
import com.qeema.engineering.service.OrderService;
import com.qeema.engineering.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Override
    public void addOrder(OrderDTO orderDTO) {
        List<Product> productsTOUpdate = new ArrayList<>();

        validateProducts(orderDTO);
        for (ProductDTO product : orderDTO.getProductList()) {
            Optional<Product> existProduct = productService.getProductByID(product);
            if (existProduct.isPresent()) {
                if (product.getQuantity() <= existProduct.get().getQuantity()) {
                    existProduct.get().setQuantity(existProduct.get().getQuantity() - product.getQuantity());
                    productsTOUpdate.add(existProduct.get());
                } else
                    throw new IllegalArgumentException("Product quantity less than or equal to product quantity");
            } else
                throw new IllegalArgumentException("Product Not Found");
        }
        Order order = orderMapper.mapFromOrderDtoToOrderEntity(orderDTO);
        handelCreatTheOrder(order);
        updateTheProducts(productsTOUpdate);
    }

    @Override
    public List<OrderDTO> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                orderDTOs.add(orderMapper.mapFromOrderEntityToOrderDTO(order));
            }
        }
        return orderDTOs;
    }


    private void validateProducts(OrderDTO order) {
        Set<Long> productIds = new HashSet<>();
        for (ProductDTO product : order.getProductList()) {
            if (product.getId() == null) {
                throw new IllegalArgumentException("missing product ID");
            }
            if (product.getPrice() <= 0) {
                throw new IllegalArgumentException("Product price must be greater than 0.");
            }
            if (product.getQuantity() <= 0) {
                throw new IllegalArgumentException("Product quantity must be greater than 0");
            }
            if (!productIds.add(product.getId())) {
                throw new IllegalArgumentException("Each product must exist only one time in the order.");
            }
        }
    }

    @Transactional
    protected void handelCreatTheOrder(Order order) {
        orderRepository.save(order);
    }

    private void updateTheProducts(List<Product> products) {
        productService.updateProducts(products);
    }

}
