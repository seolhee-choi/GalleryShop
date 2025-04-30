package com.example.gallery.backend.dto;

import lombok.Getter;

@Getter
public class Item {

    private int id;

    private String name;

    private String imgPath;

    private int price;

    private int discountPer;

}
