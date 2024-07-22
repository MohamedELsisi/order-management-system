package com.qeema.engineering.config;

import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.Product;
import com.qeema.engineering.repository.OrderRepository;
import com.qeema.engineering.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(ProductRepository productRepo, OrderRepository orderRepo) {
        return args -> {
            // Create and save products
            productRepo.save(new Product(1L, 1999, 100, "Product A"));
            productRepo.save(new Product(2L, 2999, 50, "Product B"));
            productRepo.save(new Product(3L, 999, 200, "Product C"));
            productRepo.save(new Product(4L, 4999, 30, "Product D"));
            productRepo.save(new Product(5L, 1549, 80, "Product E"));
            productRepo.save(new Product(6L, 5999, 20, "Product F"));
            productRepo.save(new Product(7L, 599, 500, "Product G"));
            productRepo.save(new Product(8L, 1299, 150, "Product H"));
            productRepo.save(new Product(9L, 2299, 60, "Product I"));
            productRepo.save(new Product(10L, 9999, 10, "Product J"));

        };
    }
}
