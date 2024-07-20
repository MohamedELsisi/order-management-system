package com.qeema.engineering.service;

import com.qeema.engineering.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    void addOrder(OrderDTO orderDTO);

    List<OrderDTO> getOrders();
}
