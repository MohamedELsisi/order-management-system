package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful created")})
    public ResponseEntity<Void> createOrder(@Validated @RequestBody OrderDTO orderDTO) {
        orderService.createNewOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful Retrieve")})
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }
}
