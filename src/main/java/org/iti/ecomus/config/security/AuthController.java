package org.iti.ecomus.config.security;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.UserSignInDTO;
import org.iti.ecomus.dto.UserSignUpDTO;
import org.iti.ecomus.exceptions.UnauthorizedException;
import org.iti.ecomus.mappers.UserMapper;
import org.iti.ecomus.util.MailMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.iti.ecomus.entity.User;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication, registration and refresh token Generation")
public class AuthController {
    @Autowired
    UserDetailsManager userDetailsManager;
    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private MailMessenger mailMessenger;


    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<Token> register(@RequestBody @Valid UserSignUpDTO signupDTO,  HttpServletResponse response) {
        User user = userMapper.toUserFromSignUp(signupDTO);
        userDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        Token token = tokenGenerator.createToken(authentication);

        // Set refresh token as HTTP-only cookie
        setRefreshTokenCookie(response, token.getRefreshToken());

        // Send a welcome email
        mailMessenger.successfullyRegister(signupDTO.getUserName(), signupDTO.getEmail());

        return ResponseEntity.ok(token);
    }



    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<Token> login(@RequestBody @Valid UserSignInDTO loginDTO,HttpServletResponse response) {
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getEmail(), loginDTO.getPassword()));

        Token token = tokenGenerator.createToken(authentication);

        // Set refresh token as HTTP-only cookie
        setRefreshTokenCookie(response, token.getRefreshToken());

        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/token", produces = "application/json")
    public ResponseEntity<Token> token(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw  new UnauthorizedException("No refresh token found");
        }


        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(refreshToken));

        Token token = tokenGenerator.createToken(authentication);

        Jwt jwt = (Jwt) authentication.getCredentials();

        // Set new refresh token as HTTP-only cookie
        setRefreshTokenCookie(response, token.getRefreshToken());

        // check if present in db and not revoked, etc
        return ResponseEntity.ok(token);

    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        // Clear the refresh token cookie
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }
    // Helper method to set refresh token cookie
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);  // Cannot be accessed by JavaScript
        cookie.setSecure(true);    // Only sent over HTTPS
        cookie.setPath("/api/auth"); // Only sent to auth endpoints
        cookie.setMaxAge(AppConstants.JWT_Refresh_TOKEN_VALIDITY); // 30 days in seconds
//        cookie.setSameSite(Cookie.SameSite.STRICT); // CSRF protection
        response.addCookie(cookie);
    }

    // Helper method to extract refresh token from cookie
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // Helper method to clear refresh token cookie
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);
    }

}