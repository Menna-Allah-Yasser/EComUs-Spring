package org.iti.ecomus.entity;

// todo --> password hash
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"addresses", "orders", "carts"})
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "userName", nullable = false )
//    @NotEmpty
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
//    @Email
//    @NotEmpty
    private String email;

    @Column(name = "password", nullable = false)
//    @NotEmpty
    private String password;

    @Column(name = "birthdate")
    private Date BD;

    @Column(name = "job")
    private String job;

    @Column(name = "creditNo")
    private String creditNo;

    @Column(name = "creditLimit")
    private BigDecimal creditLimit;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;


    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true ,
            fetch = FetchType.LAZY)
    private List<Order> orders;

        @OneToMany(mappedBy = "user",
        cascade =CascadeType.ALL,
orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Cart> carts;


    public User() {
    }

    public User(
            String userName, String email, String password, Date BD, String job, String creditNo, BigDecimal creditLimit, String phone) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.BD = BD;
        this.job = job;
        this.creditNo = creditNo;
        this.creditLimit = creditLimit;
        this.phone = phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" +this.getRole().toString());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}