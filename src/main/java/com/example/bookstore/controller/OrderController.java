package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderDto;
import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order controller", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final UserDetailsService userDetailsService;
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        return orderService.createOrder(user, orderRequestDto);
    }
}
