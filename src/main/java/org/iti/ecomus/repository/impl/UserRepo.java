package org.iti.ecomus.repository.impl;

import com.sun.jdi.InterfaceType;
import org.iti.ecomus.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;


public interface UserRepo extends ListCrudRepository<User, Integer> {

}
