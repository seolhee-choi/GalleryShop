package com.example.gallery.backend.dto.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductStat implements ChartStat {
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
}