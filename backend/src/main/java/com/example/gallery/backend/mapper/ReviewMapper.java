package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> findByItemIdOrderByUpdatedDateDesc(@Param("itemId") int itemId);

    void save(Review review);

    List<Review> findAllReview();

    void updateReview(Review reviews);
}
