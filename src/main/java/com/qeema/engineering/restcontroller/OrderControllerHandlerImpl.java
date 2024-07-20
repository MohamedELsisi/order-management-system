package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderControllerHandlerImpl implements OrderControllerHandler {
    @Autowired
    OrderService orderService;


    @Override
    public ResponseEntity<Void> handleCreateOrder(OrderDTO order) {

        if (order.getProductList() == null || order.getProductList().isEmpty()) {
            throw new IllegalArgumentException("The order must contain at least one product.");
        }

        orderService.addOrder(order);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }
}
