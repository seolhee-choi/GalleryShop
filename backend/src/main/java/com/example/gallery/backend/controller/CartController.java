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
    CartMapper cartMapper;

    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/api/cart/items")
    public ResponseEntity<ApiResponse<List<Item>>> getCartItems(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Member member) {
            int userId = member.getId(); // 여기서 바로 ID 꺼내기
            // 장바구니 서비스로부터 해당 유저의 카트 아이템 조회
            List<Cart> cartItems = cartMapper.findByMemberId(userId);

            if (cartItems.isEmpty()) {
                throw new BizException(ErrorCode.ERROR_013);
            }

            List<Integer> itemIds = cartItems.stream().map(Cart::getItemId).toList();
            List<Item> items = itemMapper.findByIdIn(itemIds, userId);

            // 아이템이 없을 경우 에러 발생
            if (items.isEmpty()) {
                throw new BizException(ErrorCode.ERROR_013);
            }

            return ResponseFactory.success(items);

        }
        throw new BizException(ErrorCode.ERROR_001);
    }


    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity<ApiResponse<Cart>> pushCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        int memberId = jwtService.getId(token);
        Cart cart = cartMapper.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            newCart.setQuantity(1);
            cartMapper.insertCart(newCart);
        }

        return ResponseFactory.success(cart);
    }


    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        int memberId = jwtService.getId(token);
//        Cart cart = cartMapper.findByMemberIdAndItemId(memberId, itemId);

        cartMapper.deleteByMemberIdAndItemId(memberId, itemId);
        return ResponseFactory.success(null);
    }
}