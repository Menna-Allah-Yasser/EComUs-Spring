package org.iti.ecomus.repository;

import com.sun.jdi.InterfaceType;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long>, SearchRepository<User,Long> {


    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email")
    Long checkValidEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);

    @Override
    default public Specification<User> getKeywordSpecification(String keyword) {
        return UserSpecification.containsKeyword(keyword);
    }

    @Override
    default public Specification<User> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return UserSpecification.build(keyword, searchParams);
    }
}
