package org.iti.ecomus.config.security;

import java.util.Collections;

import org.iti.ecomus.entity.User;
import org.iti.ecomus.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JWTtoUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        User user = userRepo.findById(Long.parseLong(source.getSubject())).get();
//        user.setUserId(Long.parseLong(source.getSubject()));

//        UserDetails userDetails = new MyUserDetails(user);
        return new UsernamePasswordAuthenticationToken(user, source, user.getAuthorities());
    }


}