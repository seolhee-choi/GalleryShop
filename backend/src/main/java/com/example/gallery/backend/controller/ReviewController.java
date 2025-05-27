package com.example.gallery.backend.controller;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Review;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.ReviewMapper;
import com.example.gallery.backend.response.ApiResponse;
import com.example.gallery.backend.response.ResponseFactory;
import com.example.gallery.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;


    @GetMapping("/api/reviews/{itemId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReview(@PathVariable("itemId") int itemId){
        return ResponseFactory.success(reviewService.getReviewsByItemId(itemId));
    }

    @GetMapping("/api/reviews/find/{authorId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewById(@PathVariable("authorId") int authorId){
        return ResponseFactory.success(reviewService.getReviewsByAuthorId(authorId));
    }

    @Transactional
    @PostMapping("/api/reviews/register/{itemId}")
    public ResponseEntity<ApiResponse<Boolean>> registerReview(@PathVariable("itemId") int itemId, @RequestBody Review dto, @CookieValue(value = "token", required = false) String token) {
        return ResponseFactory.success(reviewService.registerReview(itemId, dto, token));
    }


    // 관리자 - 리뷰 조회
    @GetMapping("/api/admin/reviews")
    public ResponseEntity<ApiResponse<List<Review>>> reviewList(){
        return ResponseFactory.success(reviewService.getAllReviews());
    }

    // 관리자 - 리뷰 업데이트
    @PostMapping("/api/admin/reviews")
    public ResponseEntity<ApiResponse<Boolean>> updateReviews(@RequestBody List<Review> reviews) {
        return ResponseFactory.success(reviewService.updateReviews(reviews));
    }
}