package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    private int id;

    private String name;

    private String imgPath;

    private int price;

    private int discountPer;

    private int quantity;

    private int status;
}
