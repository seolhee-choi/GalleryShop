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
    OrderMapper orderMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    OrderService orderService;

    @GetMapping("/api/orders")
//    public ResponseEntity<ApiResponse<List<Order>>> getOrder(@CookieValue(value = "token", required = false) String token) {
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrder(@CookieValue(value = "token", required = false) String token) {

        int memberId = jwtService.getId(token);

        List<Order> orders = orderMapper.findByMemberIdOrderByIdDesc(memberId);

        if(orders.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_018);
        }

        List<Map<String, Object>> ordersWithItems = new ArrayList<>();
        for (Order order : orders) {

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("order", order);
            orderData.put("items", order.getItemList());

            ordersWithItems.add(orderData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersWithItems);

        return ResponseFactory.success(response);
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