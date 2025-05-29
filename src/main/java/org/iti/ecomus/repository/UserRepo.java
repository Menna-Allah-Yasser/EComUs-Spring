package org.iti.ecomus.repository;

import com.sun.jdi.InterfaceType;
import org.iti.ecomus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

}
