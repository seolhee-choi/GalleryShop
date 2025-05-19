package com.example.gallery.backend.dto.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DailyStat implements ChartStat{
    private String day; // "2025-05-16" 같은 포맷
    private BigDecimal totalPrice;
}
