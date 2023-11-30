package com.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.bookstore.model.CartItem;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName(value = "Get cart item by id and shopping cart id")
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/books/delete-all-books.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/books/insert-five-books.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql",
            "classpath:database/cart-items/insert-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findCartItemByIdAndShoppingCartId_ValidIds_ReturnList() {
        Long shoppingCartId = 1L;
        Long cartItemId = 2L;
        Optional<CartItem> cartItem = cartItemRepository
                .findCartItemByIdAndShoppingCartId(cartItemId, shoppingCartId);
        assertThat(cartItem.get())
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("shoppingCart.id", 1L)
                .hasFieldOrPropertyWithValue("book.id", 2L)
                .hasFieldOrPropertyWithValue("quantity", 3);

    }

    @Test
    @DisplayName(value = "Get cart item shopping cart id and book id")
    @Sql(scripts = {"classpath:database/cart-items/delete-all-cart-items.sql",
            "classpath:database/books/delete-all-books.sql",
            "classpath:database/shopping-cart/delete-all-carts.sql",
            "classpath:database/books/insert-five-books.sql",
            "classpath:database/shopping-cart/insert-shopping-cart.sql",
            "classpath:database/cart-items/insert-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByShoppingCartIdAndBookId_ValidIds_ReturnList() {
        Long shoppingCartId = 1L;
        Long bookId = 2L;
        Optional<CartItem> cartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCartId, bookId);
        assertThat(cartItem.get())
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("shoppingCart.id", 1L)
                .hasFieldOrPropertyWithValue("book.id", 2L)
                .hasFieldOrPropertyWithValue("quantity", 3);
    }
}
