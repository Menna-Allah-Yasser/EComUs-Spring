package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OrderDetailsMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userName", source = "user.userName")
    OrderDTO toOrderDTO(Order order);
    Order toOrder(OrderDTO orderDTO);

    List<OrderDTO> toOrderDTO(List<Order> orders);
    List<Order> toOrder(List<OrderDTO> orderDTOs);
}
