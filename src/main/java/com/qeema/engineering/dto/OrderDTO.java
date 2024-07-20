package com.qeema.engineering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class OrderDTO {

    @JsonProperty("products")
    List<ProductDTO> productList;

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        this.productList = productList;
    }
}
