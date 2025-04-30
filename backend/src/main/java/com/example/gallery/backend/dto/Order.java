package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private int id;

    private int memberId;

    private String name;

    private String address;

    private String payment;

    private String cardNumber;

    private String items;
}
