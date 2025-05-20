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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/api/orders")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrders(@CookieValue(value = "token", required = false) String token) {
        return ResponseFactory.success(orderService.getOrdersWithItems(token));
    }


    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity<ApiResponse<Boolean>> pushOrder(@RequestBody Order dto, @CookieValue(value = "token", required = false) String token) {
        return ResponseFactory.success(orderService.createOrder(dto, token));
    }

    // 관리자 - 주문 조회
    @GetMapping("/api/admin/orders")
    public ResponseEntity<ApiResponse<Map<String, Object>>> orderList() {
        return ResponseFactory.success(orderService.getAllOrdersWithItems());
    }

    // 관리자 - 인기 상품 조회
    @GetMapping("/api/admin/topOrderList")
    public ResponseEntity<ApiResponse<Map<String, Object>>> topOrderList() {
        return ResponseFactory.success(orderService.getTopProducts());
    }

}