package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

//    @Mapping(source = "user.userId", target = "userId")
    AddressDTO toDto(Address address);

//    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    Address toEntity(AddressDTO addressDTO);

//    @Named("mapUserIdToUser")
//    default User mapUserIdToUser(Long id) {
//        if (id == null) return null;
//        User user = new User();
//        user.setUserId(id);
//        return user;
//    }

    List<AddressDTO> toDtoList(List<Address> addressList);

    List<Address> toEntityList(List<AddressDTO> addressDtoList);
}
