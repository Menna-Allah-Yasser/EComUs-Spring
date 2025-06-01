package org.iti.ecomus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.iti.ecomus.dto.CategoryDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class CategoryAdminControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getAllCategories_ShouldReturnCategories() throws Exception {
        mockMvc.perform(get("/api/admin/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getCategoryById_WithValidId_ShouldReturnCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO(null, "Test Get", null);

        MvcResult result = mockMvc.perform(post("/api/admin/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);

        mockMvc.perform(get("/api/admin/category/" + created.getCategoryId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryId").value(created.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value("Test Get"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createCategory_WithValidData_ShouldCreateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Test Category");

        mockMvc.perform(post("/api/admin/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Test Category"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_shouldSucceed() throws Exception {
        CategoryDTO dto = new CategoryDTO(null, "Sports", null);

        mockMvc.perform(post("/api/admin/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Sports"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategory_WithValidId_ShouldDeleteCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO(null, "To Be Deleted", null);

        MvcResult result = mockMvc.perform(post("/api/admin/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDTO savedCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDTO.class
        );

        mockMvc.perform(delete("/api/admin/category/" + savedCategory.getCategoryId())
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/category/" + savedCategory.getCategoryId()))
                .andExpect(status().isNotFound());
    }
}