package com.example.gallery.backend.mapper;

import com.example.gallery.backend.dto.Member;
import com.example.gallery.backend.dto.Order;
import com.example.gallery.backend.dto.Review;
import com.example.gallery.backend.dto.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> findByItemIdOrderByUpdatedDateDesc(@Param("itemId") int itemId);

    List<Review> findByAuthorIdOrderByUpdatedDateDesc(@Param("authorId") int authorId);

    int countReview(Search params);
    void save(Review review);

    List<Review> findAllReview(@Param("search") Search search);

    void updateReview(Review reviews);
}
