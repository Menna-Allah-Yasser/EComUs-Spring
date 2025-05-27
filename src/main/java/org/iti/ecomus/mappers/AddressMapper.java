package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDto(Address address);
    Address toEntity(AddressDTO addressDTO);
}
