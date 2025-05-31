package org.iti.ecomus.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.specification.UserSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;


//@Configuration
//public class    SearchRepoConfig {
//
////    @Bean
////    public SearchRepositoryImpl<User, Long> userSearchRepositoryImpl(EntityManager em, UserSpecification userSpec, EntityManagerFactory emf) {
////        JpaEntityInformation<User, Long> entityInfo = new JpaMetamodelEntityInformation<>(User.class, em.getMetamodel(), emf.getPersistenceUnitUtil());
////        return new SearchRepositoryImpl<>(entityInfo, em, userSpec);
////    }
//}