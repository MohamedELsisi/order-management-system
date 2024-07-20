package com.qeema.engineering.restcontroller;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.exception.ValidationException;
import com.qeema.engineering.service.OrderService;
import com.qeema.engineering.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderControllerHandlerImpl implements OrderControllerHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerHandlerImpl.class.getName());


    @Autowired
    OrderService orderService;


    @Override
    public ResponseEntity<Void> handleCreateOrder(OrderDTO order) {

        if (order.getProductList() == null || order.getProductList().isEmpty()) {
            logger.error("Product list is empty");
            throw new ValidationException("The order must contain at least one product.");
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
        logger.info("Validating product list");
        Set<Long> productIds = new HashSet<>();
        for (ProductDTO product : order.getProductList()) {
            if (product.getId() == null) {
                logger.error("Product id is empty");
                throw new ValidationException("missing product ID");
            }
            if (product.getPrice() <= 0) {
                logger.error("Product price less than or equal 0");
                throw new ValidationException("Product price must be greater than 0.");
            }
            if (product.getQuantity() <= 0) {
                logger.error("Product quantity less than or equal 0");
                throw new ValidationException("Product quantity must be greater than 0");
            }
            if (!productIds.add(product.getId())) {
                logger.error("Duplicate product id " + product.getId());
                throw new ValidationException("Each product must exist only one time in the order.");
            }
        }
    }
}
