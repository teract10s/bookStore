package com.example.bookstore.service.impl;

import com.example.bookstore.dto.cart.items.CartItemDto;
import com.example.bookstore.dto.cart.items.CreateCartItemRequestDto;
import com.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.security.CustomUserDetailsService;
import com.example.bookstore.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Authentication authentication) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto addCartItemToShoppingCart(
            CreateCartItemRequestDto cartItemRequestDto, Authentication authentication
    ) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        Optional<CartItem> cartItemFromDB = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), cartItemRequestDto.bookId());
        if (cartItemFromDB.isPresent()) {
            CartItem cartItem = cartItemFromDB.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDto.quantity());
            return cartItemMapper.toDto(cartItemRepository.save(cartItem));
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setShoppingCart(shoppingCart);
        newCartItem.setBook(bookRepository.getReferenceById(cartItemRequestDto.bookId()));
        newCartItem.setQuantity(cartItemRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(newCartItem));
    }

    @Override
    public CartItemDto updateQuantityOfCartItem(Authentication authentication, Long cartItemId,
                                                int quantity) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        CartItem cartItem = cartItemRepository.findCartItemByIdAndShoppingCartId(cartItemId,
                        user.getShoppingCart().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId + " in your shopping cart"));
        cartItem.setQuantity(quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Authentication authentication, Long cartItemId) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        CartItem cartItem = cartItemRepository.findCartItemByIdAndShoppingCartId(cartItemId,
                        user.getShoppingCart().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId + " in your shopping cart"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public ShoppingCartDto save(ShoppingCart shoppingCart) {
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }
}
