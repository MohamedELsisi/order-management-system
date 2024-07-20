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
            Product productA = new Product();
            productA.setName("Product A");
            productA.setPrice(10);
            productA.setQuantity(100);
            productRepo.save(productA);

            Product productB = new Product();
            productB.setName("Product B");
            productB.setPrice(1550);
            productB.setQuantity(150);
            productRepo.save(productB);

        };
    }
}
