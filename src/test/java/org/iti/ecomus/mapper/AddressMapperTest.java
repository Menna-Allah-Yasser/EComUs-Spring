package org.iti.ecomus.mapper;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.Address;
import org.iti.ecomus.mappers.AddressMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
@IntegrationTest
public class AddressMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    void toAddress() {
        // Given
        // Create a sample UserAddressDTO object
        AddressDTO addressDto = new AddressDTO();
        addressDto.setId(1L);
        addressDto.setStreet("123 Main St");
        addressDto.setCity("Springfield");
        addressDto.setBuildingNo("A1");
        addressDto.setArea("Downtown");

        // When
        // Convert DTO to entity using AddressMapper
        Address userAddressEntity = addressMapper.toEntity(addressDto);

        // Then
        assertNotNull(userAddressEntity);
        assertEquals(1L, userAddressEntity.getId());
        assertEquals("123 Main St", userAddressEntity.getStreet());
        assertEquals("Springfield", userAddressEntity.getCity());
        assertEquals("A1", userAddressEntity.getBuildingNo());
        assertEquals("Downtown", userAddressEntity.getArea());
        assertNull(userAddressEntity.getUser()); // Assuming user is not set in this test
    }

    @Test
    void toAddressDTO() {
        // Given
        // Create a sample UserAddress entity object
        Address userAddressEntity = new Address();
        userAddressEntity.setId(1L);
        userAddressEntity.setStreet("123 Main St");
        userAddressEntity.setCity("Springfield");
        userAddressEntity.setBuildingNo("A1");
        userAddressEntity.setArea("Downtown");

        // When
        // Convert entity to DTO using AddressMapper
        AddressDTO addressDto = addressMapper.toDto(userAddressEntity);

        // Then
        assertNotNull(addressDto);
        assertEquals(1L, addressDto.getId());
        assertEquals("123 Main St", addressDto.getStreet());
        assertEquals("Springfield", addressDto.getCity());
        assertEquals("A1", addressDto.getBuildingNo());
        assertEquals("Downtown", addressDto.getArea());
    }
}
