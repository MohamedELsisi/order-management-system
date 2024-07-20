package com.qeema.engineering.mapper;


import com.qeema.engineering.dto.OrderDTO;
import com.qeema.engineering.model.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "productList", source = "orderProducts")
    OrderDTO mapFromOrderEntityToOrderDTO(Order order);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "orderProducts", source = "productList")
   Order mapFromOrderDtoToOrderEntity(OrderDTO  orderDTO);
}

