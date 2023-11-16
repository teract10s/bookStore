package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.category.CategoryResponseDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Find category by existent id")
    @WithMockUser(username = "admin", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/categories/delete-all-categories.sql",
            "classpath:database/categories/insert-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCategoryById_WithExistentId_Success() throws Exception {
        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Historical")
                .hasFieldOrPropertyWithValue("description", "Some books about history");
    }

    @Test
    @DisplayName("Create category")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/categories/delete-all-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createCategory_ValidInput_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("new category",
                "some description");

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "new category")
                .hasFieldOrPropertyWithValue("description", "some description");
    }

    @Test
    @DisplayName("Update category")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/categories/delete-all-categories.sql",
            "classpath:database/categories/insert-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateCategories_ValidInput_Success() throws Exception {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "UpdateById", "New Description");

        MvcResult result = mockMvc.perform(put("/categories/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "UpdateById")
                .hasFieldOrPropertyWithValue("description", "New Description");
    }

    @Test
    @DisplayName("Delete category with existent id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/categories/delete-all-categories.sql",
            "classpath:database/categories/insert-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategories_WithExistentId_Success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/categories/" + id))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
