package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findAddressByUserUserId(Long userId);
}
