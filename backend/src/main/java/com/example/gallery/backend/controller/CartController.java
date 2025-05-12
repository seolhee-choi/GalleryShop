package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Cart;
import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.ResultVO;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    JwtService jwtService;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/api/cart/items")
//    public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {
//
//        if (!jwtService.isValid(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//
//        int memberId = jwtService.getId(token);
//        List<Cart> carts = cartMapper.findByMemberId(memberId);
//        List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();
//        List<Item> items = itemMapper.findByIdIn(itemIds, memberId);
//
//        return new ResponseEntity<>(items, HttpStatus.OK);
//
//    }

    public ResponseEntity getCartItems(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Member member) {
            int userId = member.getId(); // 여기서 바로 ID 꺼내기
            // 장바구니 서비스로부터 해당 유저의 카트 아이템 조회
            List<Cart> cartItems = cartMapper.findByMemberId(userId);
            List<Integer> itemIds = cartItems.stream().map(Cart::getItemId).toList();
            List<Item> items = itemMapper.findByIdIn(itemIds, userId);

            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보 없음");
    }


    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartMapper.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            newCart.setQuantity(1);
            cartMapper.insertCart(newCart);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity removeCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartMapper.findByMemberIdAndItemId(memberId, itemId);

        cartMapper.deleteByMemberIdAndItemId(memberId, itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}