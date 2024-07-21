package com.qeema.engineering.service.impl;

import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.exception.ResourceException;
import com.qeema.engineering.exception.ValidationException;
import com.qeema.engineering.mapper.OrderMapper;
import com.qeema.engineering.mapper.OrderProductMapper;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.OrderProduct;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.service.OrderService;
import com.qeema.engineering.service.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class.getName());

    OrderRepository orderRepository;
    ProductService productService;
    OrderMapper orderMapper;
    OrderProductMapper orderProductMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, OrderMapper orderMapper, OrderProductMapper orderProductMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderMapper = orderMapper;
        this.orderProductMapper = orderProductMapper;
    }

    @Override
    public CompletableFuture<Void> createNewOrder(OrderDTO orderDTO) {
        List<Product> productsToUpdate = new ArrayList<>();
        Order order = new Order();
        validateProducts(orderDTO);
        return CompletableFuture.runAsync(() -> {
            orderDTO.getProductList().forEach(productDTO -> {
                Product existingProduct = extractAndValidateProduct(productDTO);
                existingProduct.setQuantity(existingProduct.getQuantity() - productDTO.getQuantity());
                productsToUpdate.add(existingProduct);
                OrderProduct orderProduct = orderProductMapper.productDtoToOrderProduct(productDTO, order, existingProduct);
                order.getOrderProducts().add(orderProduct);
            });

            handelSaveTheOrder(order, productsToUpdate);
        });
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        logger.info("get all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setProductList(order.getOrderProducts().stream()
                            .map(orderProduct -> {
                                Product product = orderProduct.getProduct();
                                return new ProductDTO(product.getId(), product.getName(), product.getPrice(), orderProduct.getQuantity());
                            })
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Product extractAndValidateProduct(ProductDTO productDTO) {
        Product existingProduct = getProductFromDB(productDTO);
        checkQuantityOFProduct(productDTO, existingProduct);
        return existingProduct;
    }

    private Product getProductFromDB(ProductDTO product) {
        logger.info("get product {} from database", product.getId());
        return productService.getProductByID(product.getId()).orElseThrow(() -> {
            logger.info("Product {} not found", product.getId());
            return new ResourceException("Product Not Found with ID: " + product.getId());
        });
    }

    private void checkQuantityOFProduct(ProductDTO productDTO, Product existingProduct) {
        if (productDTO.getQuantity() > existingProduct.getQuantity()) {
            logger.error("product {} is over quantity", productDTO.getId());
            throw new ResourceException("Product quantity is less than the requested quantity");
        }
    }

    @Transactional
    protected void handelSaveTheOrder(Order order, List<Product> products) {

        try {
            logger.info("save the new order");
            orderRepository.save(order);

            logger.info("update the products");
            productService.updateProducts(products);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            //here we can send an event
        }

    }

    private void validateProducts(OrderDTO order) {
        logger.info("Validating product list");
        Set<Long> productIds = new HashSet<>();

        if (order.getProductList() == null || order.getProductList().isEmpty()) {
            logger.error("Product list is empty");
            throw new ValidationException("The order must contain at least one product.");
        }

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
                logger.error("Duplicate product id {}", product.getId());
                throw new ValidationException("Each product must exist only one time in the order.");
            }
        }
    }

}
