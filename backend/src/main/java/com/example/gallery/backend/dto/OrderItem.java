package com.example.gallery.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderItem {

//    private int id;

    private int orderId;

    @JsonProperty("id")
    private int itemId;

    @JsonProperty("name")
    private String itemName;

    @JsonProperty("price")
    private int priceAtOrder;

    private int discountPer;

    private int quantity;

    private Timestamp createdAt;
}
