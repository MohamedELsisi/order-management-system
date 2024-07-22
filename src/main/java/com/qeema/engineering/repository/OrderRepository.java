package com.qeema.engineering.repository;

import com.qeema.engineering.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT a FROM order_table a JOIN a.orderProducts op WHERE op.product.id IN :productIds")
    List<Order> findByProductIds(@Param("productIds") List<Long> productIds);
}
