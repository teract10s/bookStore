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
import com.example.bookstore.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(id);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto addCartItemToShoppingCart(
            CreateCartItemRequestDto cartItemRequestDto, User user
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        Optional<CartItem> cartItemFromDB = cartItemRepository
                .findByShoppingCartIdAndBookId(
                        shoppingCart.getId(),
                        cartItemRequestDto.bookId());
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
    public CartItemDto updateQuantityOfCartItem(User user, Long cartItemId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        CartItem cartItem = cartItemRepository
                .findCartItemByIdAndShoppingCart(cartItemId, shoppingCart)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId
                                + " and user_id: " + user.getId()));
        cartItem.setQuantity(quantity);
        CartItemDto result = cartItemMapper.toDto(cartItem);
        cartItemRepository.save(cartItem);
        return result;
    }

    @Override
    public void deleteCartItem(User user, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        cartItemRepository.findCartItemByIdAndShoppingCart(cartItemId, shoppingCart)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id: " + cartItemId
                                + " and user_id: " + user.getId()));
        cartItemRepository.deleteById(cartItemId);
    }
}
