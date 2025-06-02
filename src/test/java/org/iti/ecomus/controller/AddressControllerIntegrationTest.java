package org.iti.ecomus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.iti.ecomus.dto.AddressDTO;
import org.iti.ecomus.entity.Address;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.UserRole;
import org.iti.ecomus.repository.AddressRepo;
import org.iti.ecomus.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class AddressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Sql(statements = {
            "INSERT INTO user ( email, password, role, userName) " +
                    "VALUES ( 'user1@example.com', '{noop}password', 'USER', 'Test User');"
    })
    @WithUserDetails("user1@example.com")
    public void shouldCreateAndReturnAddress() throws Exception {
        AddressDTO dto = new AddressDTO(null, "Cairo", "Nasr City", "Street 1", "B5");

        mockMvc.perform(post("/api/public/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Cairo"));

        List<Address> addresses = addressRepo.findAll();
        assertThat(addresses).hasSize(1);
        assertThat(addresses.get(0).getCity()).isEqualTo("Cairo");
    }

    @Test
    @Sql(statements = {
            "INSERT INTO user ( email, password, role, userName) " +
                    "VALUES ( 'user1@example.com', '{noop}password', 'USER', 'Test User');"
    })
    @WithUserDetails("user1@example.com")
    void shouldGetUserAddresses() throws Exception {
        User user = userRepo.findByEmail("user1@example.com").orElseThrow();
        Address address = new Address("Cairo", "Heliopolis", "Street 9", "B3");
        address.setUser(user);
        addressRepo.save(address);

        mockMvc.perform(get("/api/public/address"))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].city").value("Cairo"))
        ;
    }

    @Test
    @Sql(statements = {
            "INSERT INTO user ( email, password, role, userName) " +
                    "VALUES ( 'user1@example.com', '{noop}password', 'USER', 'Test User');"
    })
    @WithUserDetails("user1@example.com")
    void shouldUpdateAddress() throws Exception {
        User user = userRepo.findByEmail("user1@example.com").orElseThrow();
        Address address = new Address("OldCity", "OldArea", "OldStreet", "B1");
        address.setUser(user);
        address = addressRepo.save(address);

        AddressDTO updatedDTO = new AddressDTO(address.getId(), "NewCity", "NewArea", "NewStreet", "B9");

        mockMvc.perform(put("/api/public/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("NewCity"));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO user ( email, password, role, userName) " +
                    "VALUES ( 'user1@example.com', '{noop}password', 'USER', 'Test User');"
    })
    @WithUserDetails("user1@example.com")

    void shouldDeleteAddress() throws Exception {
        User user = userRepo.findByEmail("user1@example.com").orElseThrow();
        Address address = new Address("ToDelete", "A", "S", "B");
        address.setUser(user);
        Address saved = addressRepo.save(address);

        mockMvc.perform(delete("/api/public/address/" + saved.getId())
                        .param("addressId", saved.getId().toString()))
                .andExpect(status().isNoContent());

        assertThat(addressRepo.findById(saved.getId())).isEmpty();
    }
}
