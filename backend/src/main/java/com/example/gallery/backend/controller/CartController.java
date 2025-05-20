package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Cart;
import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    JwtService jwtService;

    @Autowired
    CartService cartService;

    @Autowired
    ItemMapper itemMapper;


    @GetMapping("/api/cart/items")
    public ResponseEntity<ApiResponse<List<Item>>> getCartItems(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Member member) {
            int userId = member.getId(); // 여기서 바로 ID 꺼내기
            return ResponseFactory.success(cartService.getCartItems(userId));

        }
        throw new BizException(ErrorCode.ERROR_001);
    }


    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity<ApiResponse<Cart>> pushCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {
        return ResponseFactory.success(cartService.pushCartItem(itemId, token));
    }


    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity<ApiResponse<Boolean>> removeCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {
        return ResponseFactory.success(cartService.removeCartItem(itemId, token));
    }
}