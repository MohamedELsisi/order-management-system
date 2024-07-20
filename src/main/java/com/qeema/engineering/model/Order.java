package com.qeema.engineering.model;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity(name = "order_table")
public class Order extends BaseEntity  {

    @OneToMany(cascade = CascadeType.PERSIST ,orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Product> products;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
