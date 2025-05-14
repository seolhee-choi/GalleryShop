package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
//    private List<OrderItem> items;

    private List<OrderItem> itemList;
}
