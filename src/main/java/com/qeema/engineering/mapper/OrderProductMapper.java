package com.qeema.engineering.mapper;

import com.qeema.engineering.dto.ProductDTO;
import com.qeema.engineering.model.Order;
import com.qeema.engineering.model.OrderProduct;
import com.qeema.engineering.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "quantity", source = "productDTO.quantity")
    @Mapping(target = "price", source = "productDTO.price")
    OrderProduct productDtoToOrderProduct(ProductDTO productDTO, Order order, Product product);
}
