package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    OrderMapper orderMapper;

    @GetMapping("/api/admin/members")
    public ResponseEntity<List<Member>> memberList() {
        List<Member> members = memberMapper.findAllMember();

        if(members.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_007);
        }
        return ResponseEntity.ok(members);
    }

    @GetMapping("/api/admin/orders")
    public ResponseEntity<List<Order>> orderList() {
        List<Order> orders = orderMapper.findAllOrder();

        if(orders.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_007);
        }
        return ResponseEntity.ok(orders);
    }
}
