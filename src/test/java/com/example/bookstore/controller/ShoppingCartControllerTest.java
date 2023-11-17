package com.example.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.CartItemUpdateRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collections;
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
class ShoppingCartControllerTest {
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
    @DisplayName("Get shopping cart by authenticated user")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/users/delete-all-users.sql",
            "classpath:database/users/insert-users.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getShoppingCart_WithAuthenticatedUser_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("cartItems", Collections.emptySet());
    }

    @Test
    @DisplayName("Add cart item")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/books/delete-all-books.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/users/delete-all-users.sql",
            "classpath:database/books/insert-five-books.sql",
            "classpath:database/users/insert-users.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addBookToShoppingCart_ValidRequest_Success() throws Exception {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(1L, 2);
        MvcResult result = mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemDto.class
        );
        assertThat(actual)
                .hasFieldOrPropertyWithValue("bookId", 1L)
                .hasFieldOrPropertyWithValue("bookTitle", "H1")
                .hasFieldOrPropertyWithValue("quantity", 2);
    }

    @Test
    @DisplayName("Update quantity of cart item")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/users/delete-all-users.sql",
            "classpath:database/books/insert-five-books.sql",
            "classpath:database/users/insert-users.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql",
            "classpath:database/cart-items/insert-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateCartQuantity_ValidRequest_Success() throws Exception {
        Long cartItemId = 1L;
        CartItemUpdateRequestDto requestDto = new CartItemUpdateRequestDto(5);
        MvcResult result = mockMvc.perform(put("/cart/cart-items/" + cartItemId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemDto.class
        );
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("bookId", 1L)
                .hasFieldOrPropertyWithValue("bookTitle", "H1")
                .hasFieldOrPropertyWithValue("quantity", 5);
    }

    @Test
    @DisplayName("Delete cart item")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER"})
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/books/delete-all-books.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/users/delete-all-users.sql",
            "classpath:database/books/insert-five-books.sql",
            "classpath:database/users/insert-users.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql",
            "classpath:database/cart-items/insert-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCartItem_WithExistentIdAndValidId_Success() throws Exception {
        Long cartItemId = 2L;
        mockMvc.perform(delete("/cart/cart-items/" + cartItemId))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}