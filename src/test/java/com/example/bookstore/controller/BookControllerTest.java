package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.book.BookDto;
import com.example.bookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
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
public class BookControllerTest {
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
    @DisplayName("Find book by existent id")
    @WithMockUser(username = "admin", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/books/delete-all-books.sql",
            "classpath:database/books/insert-one-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBookById_WithExistentId_Success() throws Exception {
        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/books/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "getById")
                .hasFieldOrPropertyWithValue("author", "author")
                .hasFieldOrPropertyWithValue("isbn", "isbn")
                .hasFieldOrPropertyWithValue("price", BigDecimal.TEN)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("coverImage", "cover_image");
    }

    @Test
    @DisplayName("Create book")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/books/delete-all-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_ValidInput_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto("title", "author", "isbn",
                BigDecimal.TEN, "description", "cover_image", null);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "title")
                .hasFieldOrPropertyWithValue("author", "author")
                .hasFieldOrPropertyWithValue("isbn", "isbn")
                .hasFieldOrPropertyWithValue("price", BigDecimal.TEN)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("coverImage", "cover_image");
    }

    @Test
    @DisplayName("Update book")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/books/delete-all-books.sql",
            "classpath:database/books/insert-one-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateBook_ValidInput_Success() throws Exception {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto("UpdateById", null, null, null,
                null, null, null);

        MvcResult result = mockMvc.perform(put("/books/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "UpdateById")
                .hasFieldOrPropertyWithValue("author", "author")
                .hasFieldOrPropertyWithValue("isbn", "isbn")
                .hasFieldOrPropertyWithValue("price", BigDecimal.TEN)
                .hasFieldOrPropertyWithValue("description", "description")
                .hasFieldOrPropertyWithValue("coverImage", "cover_image");
    }

    @Test
    @DisplayName("Delete book with existent id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = {"classpath:database/books/delete-all-books.sql",
            "classpath:database/books/insert-one-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBook_WithExistentId_Success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/books/" + id))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
