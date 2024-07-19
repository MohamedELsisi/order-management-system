package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import org.springframework.http.ResponseEntity;

public interface OrderControllerHandler {
    ResponseEntity<Void> handleCreateOrder(OrderDTO orderDTO);
}
