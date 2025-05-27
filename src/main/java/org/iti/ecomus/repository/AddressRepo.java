package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
