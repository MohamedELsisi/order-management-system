package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.service.OrderService;
import org.apache.coyote.BadRequestException;
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
    OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder (@Validated @RequestBody OrderDTO orderDTO){
        orderService.addOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
       return ResponseEntity.ok().body( orderService.getAllOrders());
    }
}
