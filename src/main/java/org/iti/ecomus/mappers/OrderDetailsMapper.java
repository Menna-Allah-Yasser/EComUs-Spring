package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.OrderDetailsDTO;
import org.iti.ecomus.entity.OrderDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {
    OrderDetailsDTO toOrderDetailsDTO(OrderDetails orderDetails);
    OrderDetails toOrderDetails(OrderDetailsDTO orderDetailsDTO);
}
