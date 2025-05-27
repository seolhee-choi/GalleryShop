package com.example.gallery.backend.service;

import com.example.gallery.backend.auth.JwtService;
import com.example.gallery.backend.dto.Item;
import com.example.gallery.backend.dto.Review;
import com.example.gallery.backend.dto.Search;
import com.example.gallery.backend.exception.BizException;
import com.example.gallery.backend.exception.ErrorCode;
import com.example.gallery.backend.mapper.ItemMapper;
import com.example.gallery.backend.mapper.MemberMapper;
import com.example.gallery.backend.mapper.ReviewMapper;
import com.example.gallery.backend.response.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    JwtService jwtService;


    public List<Review> getReviewsByItemId(int itemId) {
        List<Review> reviews = reviewMapper.findByItemIdOrderByUpdatedDateDesc(itemId);

        if (reviews == null || reviews.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_003);
        }

        return reviews;
    }

    public List<Review> getReviewsByAuthorId(int authorId) {
        List<Review> reviews = reviewMapper.findByAuthorIdOrderByUpdatedDateDesc(authorId);

        if (reviews == null || reviews.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_003);
        }

        return reviews;
    }

    @Transactional
    public boolean registerReview(int itemId, Review dto, String token) {
        int memberId = jwtService.getId(token);

        Review newReview = new Review();
        newReview.setAuthorId(memberId);
        newReview.setContent(dto.getContent());
        newReview.setRating(dto.getRating());
        newReview.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        newReview.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        newReview.setItemId(itemId);

        reviewMapper.save(newReview);
        return true;
    }

//    public PageResponse<List<Review>> getAllReviews(Search search) {
    public PageResponse<Review> getAllReviews(Search search) {
        List<Review> reviews = reviewMapper.findAllReview(search);
        int totalCount = reviewMapper.countReview(search);
        int totalPage = (int)Math.ceil((double) totalCount / search.getRecordSize());


        if (reviews.isEmpty()) {
            throw new BizException(ErrorCode.ERROR_003);
        }

//        return reviews;
        return new PageResponse<>(reviews, totalCount, search.getPage(), totalPage);

    }

    public boolean updateReviews(List<Review> reviews) {
        for (Review r : reviews) {
            reviewMapper.updateReview(r);
        }
        return true;
    }
}
