package com.qeema.engineering.dto;

import java.util.List;

public class OrderDTO {
    List<ProductDTO> productList;

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        this.productList = productList;
    }
}
