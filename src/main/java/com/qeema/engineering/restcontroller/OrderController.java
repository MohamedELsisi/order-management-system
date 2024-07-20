package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    OrderControllerHandler orderControllerHandler;

    @PostMapping
    public ResponseEntity<Void> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        System.out.println(orderDTO.getProductList().size());
        orderControllerHandler.handleCreateOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
       return orderControllerHandler.getAllOrders();
    }
}
