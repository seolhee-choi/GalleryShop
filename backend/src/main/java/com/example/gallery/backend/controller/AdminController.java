package com.example.gallery.backend.controller;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.OrderItem;
import com.example.gallery.backend.dto.Review;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.OrderMapper;
import com.example.gallery.backend.mapper.ReviewMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    OrderService orderService;

    // 회원 조회
    @GetMapping("/api/admin/members")
    public ResponseEntity<ApiResponse<List<Member>>> memberList() {
        List<Member> members = memberMapper.findAllMember();

        if(members.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_007);
        }
        return ResponseFactory.success(members);

    }

    // 회원 업데이트
    @PostMapping("/api/admin/members")
    public ResponseEntity<ApiResponse<Void>> updateMembers(@RequestBody List<Member> members) {
        for (Member m : members) {
            memberMapper.updateMember(m);
        }
        return ResponseFactory.success(null);
    }

    // 주문 조회
    @GetMapping("/api/admin/orders")
    public ResponseEntity<ApiResponse<Map<String, Object>>> orderList() {
        List<Order> orders = orderMapper.findAllOrder();

        if(orders.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_018);
        }

        List<Map<String, Object>> ordersWithItems = new ArrayList<>();

        // orders 테이블 items값을 JSON으로 파싱
        for (Order order : orders) {
            String itemsJson = order.getItems();
            List<OrderItem> itemList = orderService.parseItemJson(itemsJson);

            // Map을 사용하여 order와 itemList 함께 묶기
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("order", order);
            orderData.put("items", itemList);

            ordersWithItems.add(orderData);
        }

        // 전체 데이터를 Map으로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersWithItems);

        return ResponseFactory.success(response);
    }

    // 리뷰 조회
    @GetMapping("/api/admin/reviews")
    public ResponseEntity<ApiResponse<List<Review>>> reviewList(){

        List<Review> review = reviewMapper.findAllReview();

        if(review.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_003);
        }
        return ResponseFactory.success(review);
    }

    // 리뷰 업데이트
    @PostMapping("/api/admin/reviews")
    public ResponseEntity<ApiResponse<Void>> updateReviews(@RequestBody List<Review> reviews) {
        for (Review r : reviews) {
            reviewMapper.updateReview(r);
        }
        return ResponseFactory.success(null);
    }

}
