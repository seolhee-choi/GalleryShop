package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cart {

    private int id;

    private int memberId;

    private int itemId;

    private int quantity;

}
