package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class Review {

    private int reviewId;

    private int authorId;

    private String content;

    private int rating;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private int itemId;

    private String email;
}
