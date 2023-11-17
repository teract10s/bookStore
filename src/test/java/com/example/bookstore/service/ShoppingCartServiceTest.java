package com.example.bookstore.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.mapper.impl.CartItemMapperImpl;
import com.example.bookstore.mapper.impl.ShoppingCartMapperImpl;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.security.CustomUserDetailsService;
import com.example.bookstore.service.impl.BookServiceImpl;
import com.example.bookstore.service.impl.ShoppingCartServiceImpl;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Spy
    private CartItemMapper cartItemMapper = new CartItemMapperImpl();
    @Spy
    private ShoppingCartMapper shoppingCartMapper = new ShoppingCartMapperImpl(cartItemMapper);
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    private static final User DEFAULT_USER;
    private static final Book DEFAULT_BOOK;
    private static final ShoppingCart DEFAULT_SHOPPING_CART;

    static {
        DEFAULT_USER = new User();
        DEFAULT_USER.setId(1L);
        DEFAULT_USER.setEmail("email@gmail.com");

        DEFAULT_SHOPPING_CART = new ShoppingCart();
        DEFAULT_SHOPPING_CART.setId(1L);
        DEFAULT_SHOPPING_CART.setDeleted(false);
        DEFAULT_SHOPPING_CART.setUser(DEFAULT_USER);

        DEFAULT_USER.setShoppingCart(DEFAULT_SHOPPING_CART);

        DEFAULT_BOOK = new Book();
        DEFAULT_BOOK.setId(2L);
        DEFAULT_BOOK.setTitle("title");
    }

    @Test
    @DisplayName("Get shopping cart by user")
    void getShoppingCartByUserId_ValidUser_ReturnDto() {

        when(authentication.getName()).thenReturn(DEFAULT_USER.getEmail());
        when(userDetailsService.loadUserByUsername(DEFAULT_USER.getEmail())).thenReturn(DEFAULT_USER);
        when(shoppingCartRepository.findByUserId(DEFAULT_USER.getId())).thenReturn(DEFAULT_SHOPPING_CART);
        ShoppingCartDto actual = shoppingCartService.getShoppingCartByUserId(authentication);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("cartItems", null);

    }

    @Test
    @DisplayName("Add cart item with not book to shopping cart")
    void addCartItemToShoppingCart_AddNewCartItem_Success() {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(2L, 2);

        when(authentication.getName()).thenReturn(DEFAULT_USER.getEmail());
        when(userDetailsService.loadUserByUsername(DEFAULT_USER.getEmail())).thenReturn(DEFAULT_USER);
        when(shoppingCartRepository.findByUserId(DEFAULT_USER.getId())).thenReturn(DEFAULT_SHOPPING_CART);
        when(cartItemRepository.findByShoppingCartIdAndBookId(DEFAULT_SHOPPING_CART.getId(), requestDto.bookId()))
                .thenReturn(Optional.empty());
        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(1L);
        savedCartItem.setQuantity(requestDto.quantity());
        savedCartItem.setShoppingCart(DEFAULT_SHOPPING_CART);
        savedCartItem.setBook(DEFAULT_BOOK);

        when(bookRepository.getReferenceById(requestDto.bookId())).thenReturn(DEFAULT_BOOK);
        when(cartItemRepository.save(any())).thenReturn(savedCartItem);
        CartItemDto actual = shoppingCartService.addCartItemToShoppingCart(requestDto, authentication);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("quantity", 2)
                .hasFieldOrPropertyWithValue("bookId", 2L)
                .hasFieldOrPropertyWithValue("bookTitle", "title");
    }

    @Test
    @DisplayName("Add cart item with existent book to shopping cart")
    void addCartItemToShoppingCart_AddQuantityToExistentCartItem_Success() {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(2L, 2);

        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(1L);
        savedCartItem.setQuantity(3);
        savedCartItem.setShoppingCart(DEFAULT_SHOPPING_CART);
        savedCartItem.setBook(DEFAULT_BOOK);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(1L);
        updatedCartItem.setQuantity(savedCartItem.getQuantity() + requestDto.quantity());
        updatedCartItem.setShoppingCart(DEFAULT_SHOPPING_CART);
        updatedCartItem.setBook(DEFAULT_BOOK);

        when(authentication.getName()).thenReturn(DEFAULT_USER.getEmail());
        when(userDetailsService.loadUserByUsername(DEFAULT_USER.getEmail())).thenReturn(DEFAULT_USER);
        when(shoppingCartRepository.findByUserId(DEFAULT_USER.getId())).thenReturn(DEFAULT_SHOPPING_CART);
        when(cartItemRepository.findByShoppingCartIdAndBookId(DEFAULT_SHOPPING_CART.getId(), requestDto.bookId()))
                .thenReturn(Optional.of(savedCartItem));

        when(cartItemRepository.save(any())).thenReturn(updatedCartItem);
        CartItemDto actual = shoppingCartService.addCartItemToShoppingCart(requestDto, authentication);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("quantity", 5)
                .hasFieldOrPropertyWithValue("bookId", 2L)
                .hasFieldOrPropertyWithValue("bookTitle", "title");
    }

    @Test
    @DisplayName("Update quantity in existent cart item")
    void updateQuantityOfCartItem_UpdateQuantityInExistentCartItem_Success() {
        int quantity = 5;
        CartItem cartItemFromDB = new CartItem();
        cartItemFromDB.setId(1L);
        cartItemFromDB.setQuantity(3);
        cartItemFromDB.setShoppingCart(DEFAULT_SHOPPING_CART);
        cartItemFromDB.setBook(DEFAULT_BOOK);


        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(1L);
        updatedCartItem.setQuantity(cartItemFromDB.getQuantity() + quantity);
        updatedCartItem.setShoppingCart(DEFAULT_SHOPPING_CART);
        updatedCartItem.setBook(DEFAULT_BOOK);

        when(authentication.getName()).thenReturn(DEFAULT_USER.getEmail());
        when(userDetailsService.loadUserByUsername(DEFAULT_USER.getEmail())).thenReturn(DEFAULT_USER);
        when(cartItemRepository.findCartItemByIdAndShoppingCartId(cartItemFromDB.getId(), DEFAULT_SHOPPING_CART.getId()))
                .thenReturn(Optional.of(cartItemFromDB));
        when(cartItemRepository.save(any())).thenReturn(updatedCartItem);

        CartItemDto actual = shoppingCartService.updateQuantityOfCartItem(authentication, cartItemFromDB.getId(), quantity);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("quantity", 8)
                .hasFieldOrPropertyWithValue("bookId", 2L)
                .hasFieldOrPropertyWithValue("bookTitle", "title");
    }

    @Test
    @DisplayName("Delete cart item by valid id")
    void deleteCartItem_DeleteCartItemValidId_Success() {
        CartItem cartItemFromDB = new CartItem();
        cartItemFromDB.setId(1L);
        cartItemFromDB.setQuantity(3);
        cartItemFromDB.setShoppingCart(DEFAULT_SHOPPING_CART);
        cartItemFromDB.setBook(DEFAULT_BOOK);

        when(authentication.getName()).thenReturn(DEFAULT_USER.getEmail());
        when(userDetailsService.loadUserByUsername(DEFAULT_USER.getEmail())).thenReturn(DEFAULT_USER);
        when(cartItemRepository.findCartItemByIdAndShoppingCartId(cartItemFromDB.getId(), DEFAULT_SHOPPING_CART.getId()))
                .thenReturn(Optional.of(cartItemFromDB));

        shoppingCartService.deleteCartItem(authentication, cartItemFromDB.getId());
        verify(cartItemRepository).delete(cartItemFromDB);
    }

    @Test
    @DisplayName("Save shopping cart")
    void save_ValidShoppingCart_Success() {
        when(shoppingCartRepository.save(DEFAULT_SHOPPING_CART)).thenReturn(DEFAULT_SHOPPING_CART);
        ShoppingCartDto actual = shoppingCartService.save(DEFAULT_SHOPPING_CART);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("cartItems", Collections.emptySet());
    }

    @Test
    @DisplayName("Clear shopping cart")
    void clearShoppingCart_ExistentShoppingCart_Success() {
        shoppingCartService.clearShoppingCart(DEFAULT_SHOPPING_CART);
        verify(cartItemRepository).deleteAllByShoppingCartId(DEFAULT_SHOPPING_CART.getId());
    }
}