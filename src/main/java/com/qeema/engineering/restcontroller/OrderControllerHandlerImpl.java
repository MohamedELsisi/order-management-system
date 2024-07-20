package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderControllerHandlerImpl implements OrderControllerHandler {
    @Autowired
    OrderService orderService;


    @Override
    public ResponseEntity<Void> handleCreateOrder(OrderDTO order) {

        if (order.getProductList() == null || order.getProductList().isEmpty()) {
            throw new IllegalArgumentException("The order must contain at least one product.");
        }
        validateProducts(order);
        orderService.addOrder(order);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
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
}
