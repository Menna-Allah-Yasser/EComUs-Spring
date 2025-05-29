package org.iti.ecomus.repository;

import com.sun.jdi.InterfaceType;
import org.iti.ecomus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {


    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email")
    Long checkValidEmail(@Param("email") String email);

    User findByEmail(String email);

    Optional<User> findByUserName(String userName);
}
