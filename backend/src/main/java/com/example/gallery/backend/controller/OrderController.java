package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    OrderService orderService;

    @GetMapping("/api/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getOrder(@CookieValue(value = "token", required = false) String token) {

        int memberId = jwtService.getId(token);

        List<Order> orders = orderMapper.findByMemberIdOrderByIdDesc(memberId);

        return ResponseFactory.success(orders);
    }

    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity<ApiResponse<Void>> pushOrder(@RequestBody Order dto, @CookieValue(value = "token", required = false) String token) {
        List<OrderItem> items = orderService.parseItemJson(dto.getItems());

        int memberId = jwtService.getId(token);

        Order newOrder = new Order();
        newOrder.setMemberId(memberId);
        newOrder.setName(dto.getName());
        newOrder.setAddress(dto.getAddress());
        newOrder.setPayment(dto.getPayment());
        newOrder.setCardNumber(dto.getCardNumber());
        newOrder.setItems(dto.getItems());

        orderMapper.save(newOrder);
        orderMapper.saveOrderItems(newOrder.getId(), items);
        cartMapper.deleteByMemberId(memberId);

        return ResponseFactory.success(null);
    }
}