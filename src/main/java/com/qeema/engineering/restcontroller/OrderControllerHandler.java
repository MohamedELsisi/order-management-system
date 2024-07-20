package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderControllerHandler {
    ResponseEntity<Void> handleCreateOrder(OrderDTO orderDTO);

    ResponseEntity<List<OrderDTO>> getAllOrders();
}
