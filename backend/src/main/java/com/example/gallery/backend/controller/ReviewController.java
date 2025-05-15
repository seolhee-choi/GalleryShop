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
    ReviewMapper reviewMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    JwtService jwtService;


    @GetMapping("/api/reviews/{itemId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReview(@PathVariable("itemId") int itemId){

        List<Review> review = reviewMapper.findByItemIdOrderByUpdatedDateDesc(itemId);

        if(review != null) {
            return ResponseFactory.success(review);
        } else {
            throw new BizException(ErrorCode.ERROR_003);
        }
    }

    @Transactional
    @PostMapping("/api/reviews/register/{itemId}")
    public ResponseEntity<ApiResponse<Void>> registerReview(@PathVariable("itemId") int itemId, @RequestBody Review dto, @CookieValue(value = "token", required = false) String token) {

        int memberId = jwtService.getId(token);
        Review newReview = new Review();
        newReview.setAuthorId(memberId);
        newReview.setContent(dto.getContent());
        newReview.setRating(dto.getRating());
        newReview.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        newReview.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        newReview.setItemId(itemId);

        reviewMapper.save(newReview);

        return ResponseFactory.success(null);
    }

    @GetMapping("/test-error")
    public void testError() {
        System.out.println("403에러테스트");
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "테스트 401");
    }
}