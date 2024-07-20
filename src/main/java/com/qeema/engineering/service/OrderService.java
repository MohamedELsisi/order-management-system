package com.qeema.engineering.service;

import com.qeema.engineering.dto.OrderDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<Void> addOrder(OrderDTO orderDTO);
    List<OrderDTO> getOrders();
}
