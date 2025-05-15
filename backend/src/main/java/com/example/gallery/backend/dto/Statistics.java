package com.example.gallery.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {
    private String label;

    private int value;

    // 여기서부터 상품별 조회
    private String productName;

    private int quantity;

    private int totalSales;
}
