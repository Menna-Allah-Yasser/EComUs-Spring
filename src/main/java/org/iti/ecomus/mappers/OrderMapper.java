package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {OrderDetailsMapper.class})
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);
    Order toOrder(OrderDTO orderDTO);
}
