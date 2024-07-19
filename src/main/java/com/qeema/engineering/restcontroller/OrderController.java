package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @PostMapping

   public ResponseEntity<Void> createOrder(@Validated @RequestBody OrderDTO orderDTO){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
