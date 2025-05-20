package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Cart;
import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.response.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CartService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ItemMapper itemMapper;

    public List<Item> getCartItems(int userId) {
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
        return items;
    }

    public Cart pushCartItem(int itemId, String token) {
        int memberId = jwtService.getId(token);
        Cart cart = cartMapper.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            newCart.setQuantity(1);
            cartMapper.insertCart(newCart);
        }
        return cart;
    }
    public boolean removeCartItem(int itemId, String token) {
        int memberId = jwtService.getId(token);
        cartMapper.deleteByMemberIdAndItemId(memberId, itemId);

        return true;
    }
}
