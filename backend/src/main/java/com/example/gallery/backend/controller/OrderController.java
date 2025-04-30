package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.mapper.CartMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import com.example.gallery.backend.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    JwtService jwtService;


    @GetMapping("/api/orders")
    public ResponseEntity getOrder(@CookieValue(value = "token", required = false) String token) {
        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);

        List<Order> orders = orderMapper.findByMemberIdOrderByIdDesc(memberId);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity pushOrder(@RequestBody Order dto, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Order newOrder = new Order();
        newOrder.setMemberId(memberId);
        newOrder.setName(dto.getName());
        newOrder.setAddress(dto.getAddress());
        newOrder.setPayment(dto.getPayment());
        newOrder.setCardNumber(dto.getCardNumber());
        newOrder.setItems(dto.getItems());

        orderMapper.save(newOrder);
        cartMapper.deleteByMemberId(memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}