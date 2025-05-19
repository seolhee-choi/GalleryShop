package com.example.gallery.backend.dto.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MonthlyStat implements ChartStat {
    private String month;
    private BigDecimal totalPrice;
}