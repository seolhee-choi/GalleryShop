package com.example.gallery.backend.dto.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeeklyStat implements ChartStat {
    private String week; // ex: 2025-20
    private BigDecimal totalPrice;
}